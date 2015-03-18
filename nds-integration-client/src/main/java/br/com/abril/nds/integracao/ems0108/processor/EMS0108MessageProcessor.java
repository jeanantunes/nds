package br.com.abril.nds.integracao.ems0108.processor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0108.inbound.EMS0108Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.DescontoLogisticaRepository;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS0108MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private DescontoLogisticaRepository descontoLogisticaRepository;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private ParciaisService parciaisService;

	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	
	Message messageAux = new  Message();
	
	private EMS0108MessageProcessor() {

	}

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		distribuidorService.bloqueiaProcessosLancamentosEstudos();
	}

	/**
	 * Processa as linhas do arquivo da interface EMS0108
	 */
	@Override
	public void processMessage(Message message) {
		EMS0108Input input = (EMS0108Input) message.getBody();
		
		messageAux = message;
		
		/*
		ndsiLoggerFactory.getLogger().logError(
				message,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "Inicio da verificacao" )
			);
		
		System.out.println("Inicio da verificacao");
		*/
		// Verifica se existe Produto		
		Produto produto = recuperaProduto(input.getCodigoPublicacao());
		if (null == produto) {
			ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.HIERARQUIA,
                    String.format("Produto %1$s não encontrado.", input.getCodigoPublicacao())
				);
			return ;
		} /*else {
			if (!produto.getOrigem().equals(Origem.MANUAL)) {
				return;
			}
		}*/
		
		/*
		ndsiLoggerFactory.getLogger().logError(
				message,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "Inicio do Registro de Lancamento" )
			);
		*/
		//System.out.println("Inicio do Registro de Lancamento");
		
		// regra para Registro de Lancamento 		
		regraLancamento(message, input, produto);
		
		/*
		ndsiLoggerFactory.getLogger().logError(
				message,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "Inicio do Registro de Recolhimento" )
			);
		
		System.out.println("Inicio do Registro de Recolhimento");
		*/
		// regra para Registro de Recolhimento 
		regraRecolhimento(message, input);		
		
	}

	private void regraRecolhimento(Message message, EMS0108Input input) {
		if (!input.getEdicaoRecolhimento().equals(0L)) {
			ProdutoEdicao produtoEdicaoRecolhimento = this.recuperarProdutoEdicao(input.getCodigoPublicacao(), input.getEdicaoRecolhimento());		
			if (null == produtoEdicaoRecolhimento) {
				
				ndsiLoggerFactory.getLogger().logError(
						message,
						EventoExecucaoEnum.HIERARQUIA,
                        String.format("Produto %1$s Edição %2$s não cadastrada.", input.getCodigoPublicacao(), input
                                .getEdicaoRecolhimento().toString())
					);
				return;
			} else {
				Lancamento lancamento = null;
                Date dataLancamentoRecolhimentoProduto = null;

				try {
                    dataLancamentoRecolhimentoProduto = DATE_FORMAT.parse(input.getDataLancamentoRecolhimentoProduto());

				} catch (ParseException e1) {
					ndsiLoggerFactory.getLogger().logError(
							message,
							EventoExecucaoEnum.ERRO_INFRA,
							String.format( "Erro ao converter data %1$s Produto %2$s Edicão %3$s.", input.getDataLancamentoRecolhimentoProduto(), input.getCodigoPublicacao(), input.getEdicaoRecolhimento().toString() ));
					return;
				}
                lancamento = this.recuperarRecolhimento(produtoEdicaoRecolhimento, dataLancamentoRecolhimentoProduto);
				if (null != lancamento) {
					
					if (lancamento.getStatus() == StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO
						|| lancamento.getStatus() == StatusLancamento.BALANCEADO_RECOLHIMENTO
							|| lancamento.getStatus() == StatusLancamento.EM_RECOLHIMENTO
							|| lancamento.getStatus() == StatusLancamento.RECOLHIDO
							|| lancamento.getStatus() == StatusLancamento.FECHADO) {
						
						ndsiLoggerFactory.getLogger().logWarning(
								message,
								EventoExecucaoEnum.ERRO_INFRA,
                                        String.format(
                                                "Registro não será atualizado pois já está em processo de recolhimento. Data de recolhimento: %1$s Produto: %2$s Edicao: %3$s.",
                                                dataLancamentoRecolhimentoProduto, input
                                                        .getCodigoPublicacao(), input.getEdicaoRecolhimento()
                                                        .toString()));
						return;
					}
					
                    if (!lancamento.getDataRecolhimentoDistribuidor().equals(dataLancamentoRecolhimentoProduto)) {
					
						if (lancamento.getStatus().equals(StatusLancamento.BALANCEADO_RECOLHIMENTO)) {
							ndsiLoggerFactory.getLogger().logWarning(
									message,
									EventoExecucaoEnum.INF_DADO_ALTERADO,
                                            String.format(
                                                    "Não foi possivel Alterar a data devido ao status de BALANCEADO_RECOLHIMENTO, para o Produto %1$s Edicao %2$s.",
                                                    input.getCodigoPublicacao(), input.getEdicaoRecolhimento()
                                                            .toString())
								);
							return ;
						} else {
							if (dataLancamentoRecolhimentoProduto != null) {
								
								lancamento.setDataRecolhimentoDistribuidor(dataLancamentoRecolhimentoProduto);
								
								this.lancamentoService.atualizarRedistribuicoes(lancamento, dataLancamentoRecolhimentoProduto, true);
							}
							
							this.getSession().merge(lancamento);
							
							try {
								this.tratarParciais(lancamento);
							} catch (Exception e) {
								ndsiLoggerFactory.getLogger().logError(
										message,
										EventoExecucaoEnum.INF_DADO_ALTERADO,
										String.format("Erro ao processar as parcias para o Produto %1$s Edicao %2$s. " + e.getMessage(),
													  input.getCodigoPublicacao(), input.getEdicaoRecolhimento().toString() ));
								return;
							}
						}
					}
				} else {
					ndsiLoggerFactory.getLogger().logError(
							message,
							EventoExecucaoEnum.RELACIONAMENTO,
                                    String.format(
                                            "Não existe recolhimento para o Produto %1$s Edicao %2$s. Na data de lancamento %3$s",
                                            input.getCodigoPublicacao(), input.getEdicaoRecolhimento().toString(),
                                            input.getDataLancamentoRecolhimentoProduto().toString())
						);
				}

			}

		}
	}

	private void regraLancamento(Message message, EMS0108Input input,
			Produto produto) {
		/*
		ndsiLoggerFactory.getLogger().logError(
				message,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "regraLancamento(1) " )
			);
		*/
		if (!input.getEdicaoLancamento().equals(0L)) {
			ProdutoEdicao produtoEdicaoLancamento = this.recuperarProdutoEdicao(input.getCodigoPublicacao(), input.getEdicaoLancamento());		
			if (null == produtoEdicaoLancamento) {
				produtoEdicaoLancamento = inserirProdutoEdicao(input, produto);
				
				/*
				ndsiLoggerFactory.getLogger().logError(
						message,
						EventoExecucaoEnum.HIERARQUIA,
						String.format( "regraLancamento(2) " )
					);
				*/
				// no caso de inserir uma nova edicao atualiza o peso do produto aaa
				if(input.getPesoProduto()!=null && input.getPesoProduto().intValue()!=0){
				ndsiLoggerFactory.getLogger().logError(
						message,
						EventoExecucaoEnum.HIERARQUIA,
	                    String.format("Alteração de Peso"
	                    		    + " de "+produto.getPeso()
	                    		    + " para "+input.getPesoProduto()
	                    		    + " Produto "+input.getCodigoPublicacao())
					);
				produto.setPeso(input.getPesoProduto());
				}
				
				/*
				ndsiLoggerFactory.getLogger().logError(
						message,
						EventoExecucaoEnum.HIERARQUIA,
						String.format( "regraLancamento(3)" )
					);
				*/
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						String.format( "Produto %1$s Edição %2$s cadastrada. ", input.getCodigoPublicacao(), produtoEdicaoLancamento.getNumeroEdicao().toString() )
					);
				
				this.getSession().merge(produto);
				
				/*
				ndsiLoggerFactory.getLogger().logError(
						message,
						EventoExecucaoEnum.HIERARQUIA,
						String.format( "regraLancamento(4) merge" )
					);
				*/
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						String.format( "Produto %1$s Edicao %2$s cadastrada. ", input.getCodigoPublicacao(), produtoEdicaoLancamento.getNumeroEdicao().toString() )
					);
			} else {
				/*
				ndsiLoggerFactory.getLogger().logError(
						message,
						EventoExecucaoEnum.HIERARQUIA,
						String.format( "regraLancamento(5) atualizarProdutoEdicao" )
					);
				*/
				produtoEdicaoLancamento = atualizarProdutoEdicao(input, produto, produtoEdicaoLancamento);
			}
			
			Lancamento lancamento;
			
			lancamento = this.recuperarLancamento(produtoEdicaoLancamento, input.getDataMovimento());
			
			if (lancamento == null) {
                // Caso não encontre o lançamento futuro, procura o anterior
                // mais próximo com status CONFIRMADO (para alterar a data de
                // lancamento real do distribuidor) ou BALANCEADO (para não
                // fazer alterar e nem inserir um novo)
				lancamento = this.recuperarLancamentoAnteriorMaisProximo(produtoEdicaoLancamento, input.getDataMovimento());
			}

			if (null == lancamento) {
				try {
					inserirLancamento(produtoEdicaoLancamento, input);
				} catch (ParseException e) {
					ndsiLoggerFactory.getLogger().logError(
							message,
							EventoExecucaoEnum.ERRO_INFRA,
							String.format( "Erro ao converter data %1$s Produto %2$s Edição %3$s.", input.getDataLancamentoRecolhimentoProduto(), input.getCodigoPublicacao(), input.getEdicaoRecolhimento().toString() ));
					return;
				}
				
				String dataMovimento = "";
				if(input.getDataMovimento() != null){
					try{
						dataMovimento = new SimpleDateFormat("dd/MM/yyyy").format(input.getDataMovimento());
					}catch(Exception e){}
				}
				
				ndsiLoggerFactory.getLogger().logWarning(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						String.format( "Foi criado um lançamento para o Produto %1$s Edição %2$s. Na data de lançamento %3$s", input.getCodigoPublicacao(), produtoEdicaoLancamento.getNumeroEdicao().toString(), dataMovimento)
					);				
			} else {
                Date dataLancamentoRecolhimentoProduto = null;
                
                try {
                    dataLancamentoRecolhimentoProduto = DATE_FORMAT.parse(input.getDataLancamentoRecolhimentoProduto());
                    
                } catch (ParseException e1) {
                    ndsiLoggerFactory.getLogger().logError(
                            message,
                            EventoExecucaoEnum.ERRO_INFRA,
                            String.format("Erro ao converter data %1$s Produto %2$s Edição %3$s.", input
                                    .getDataLancamentoRecolhimentoProduto(), input.getCodigoPublicacao(), input
                                    .getEdicaoRecolhimento().toString()));
                }
                if (!lancamento.getDataLancamentoDistribuidor().equals(dataLancamentoRecolhimentoProduto)) {
					if (lancamento.getStatus().equals(StatusLancamento.BALANCEADO)) {
						ndsiLoggerFactory.getLogger().logWarning(
								message,
								EventoExecucaoEnum.INF_DADO_ALTERADO,
                                        String.format(
                                                "Não foi possivel Alterar a data devido ao status de BALANCEADO, para o Produto %1$s Edicao %2$s.",
                                                input.getCodigoPublicacao(), produtoEdicaoLancamento.getNumeroEdicao()
                                                        .toString())
							);	
						return ;
					} else {
						lancamento.setDataLancamentoDistribuidor(input.getDataMovimento());
						this.getSession().merge(lancamento);
						
						try {
							this.tratarParciais(lancamento);
						} catch (Exception e) {
							ndsiLoggerFactory.getLogger().logError(
									message,
									EventoExecucaoEnum.INF_DADO_ALTERADO,
									String.format("Erro ao processar as Parcias para o Produto %1$s Edição %2$s " + e.getMessage(),
												  input.getCodigoPublicacao(), produtoEdicaoLancamento.getNumeroEdicao().toString() ));
							return;
						}
					}
				}
			}
		}
	}

	private Lancamento inserirLancamento(ProdutoEdicao produtoEdicaoLancamento,
			EMS0108Input input) throws ParseException {
		Lancamento lancamento = new Lancamento();
		
		lancamento.setProdutoEdicao(produtoEdicaoLancamento);
		lancamento.setDataCriacao(new Date());
		
		
		//lancamento.setDataLancamentoDistribuidor(DATE_FORMAT.parse(input.getDataLancamentoRecolhimentoProduto()));
		//lancamento.setDataLancamentoPrevista(DATE_FORMAT.parse(input.getDataLancamentoRecolhimentoProduto()));
		
		//lancamento.setDataLancamentoDistribuidor(input.getDataMovimento());
		lancamento.setDataLancamentoPrevista(input.getDataMovimento());
		lancamento.setAlteradoInteface(true);
		lancamento.setStatus(StatusLancamento.CONFIRMADO);
		lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);

		int peb = produtoEdicaoLancamento.getPeb() == 0 ? produtoEdicaoLancamento.getProduto().getPeb() : produtoEdicaoLancamento.getPeb();
		if (peb == 0) {
			peb = 15;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(input.getDataMovimento());
		cal.add(Calendar.DATE, peb); 		
		//cal.add(Calendar.DATE, produtoEdicaoLancamento.getProduto().getPeb()); 		
		lancamento.setDataRecolhimentoDistribuidor(cal.getTime());
		lancamento.setDataRecolhimentoPrevista(cal.getTime());	
		
		try{
		 lancamento.setDataLancamentoDistribuidor(lancamentoService.obterDataLancamentoValido(input.getDataMovimento(), produtoEdicaoLancamento.getProduto().getFornecedor().getId()));
		} catch (Exception e) {
		}

		//defaults
		lancamento.setDataStatus(new Date());	
		lancamento.setReparte(BigInteger.valueOf(0));

		this.getSession().persist(lancamento);

		return lancamento;
	}

	private Lancamento recuperarLancamento(
			ProdutoEdicao produtoEdicaoLancamento, Date dataMovimento) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT lcto FROM Lancamento lcto ");
		sql.append("      JOIN FETCH lcto.produtoEdicao pe ");
		sql.append("    WHERE pe = :produtoEdicao ");
		sql.append("      AND lcto.dataLancamentoDistribuidor >= :dataMovimento ");
		sql.append("      AND lcto.tipoLancamento = :tipoLancamento ");
		sql.append(" ORDER BY lcto.dataLancamentoDistribuidor ASC");
		
		Query query = getSession().createQuery(sql.toString());
		
		query.setParameter("produtoEdicao", produtoEdicaoLancamento);
		query.setDate("dataMovimento", dataMovimento);
		query.setParameter("tipoLancamento", TipoLancamento.LANCAMENTO);
		
		query.setMaxResults(1);
		query.setFetchSize(1);
		
		return (Lancamento) query.uniqueResult();
	}

	        /**
     * Busca o lançamento anterior á data de movimetno do arquivo mais próximo
     * (busca apenas CONFIRMADO ou BALANCEADO)
     * 
     * @param produtoEdicaoLancamento
     * @param dataMovimento
     * @return
     */
	private Lancamento recuperarLancamentoAnteriorMaisProximo(
			ProdutoEdicao produtoEdicaoLancamento, Date dataMovimento) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT lcto FROM Lancamento lcto ");
		sql.append("      JOIN FETCH lcto.produtoEdicao pe ");
		sql.append("    WHERE pe = :produtoEdicao ");
		sql.append("      AND lcto.dataLancamentoDistribuidor < :dataMovimento ");
		sql.append("      AND (lcto.status = :statusConfirmado OR lcto.status = :statusBalanceado) ");
		sql.append("      AND lcto.tipoLancamento = :tipoLancamento ");
		sql.append(" ORDER BY lcto.dataLancamentoDistribuidor DESC");
		
		Query query = getSession().createQuery(sql.toString());
		
		query.setParameter("produtoEdicao", produtoEdicaoLancamento);
		query.setParameter("statusConfirmado", StatusLancamento.CONFIRMADO);
		query.setParameter("statusBalanceado", StatusLancamento.BALANCEADO);
		query.setDate("dataMovimento", dataMovimento);
		query.setParameter("tipoLancamento", TipoLancamento.LANCAMENTO);
		
		query.setMaxResults(1);
		query.setFetchSize(1);
		
		return (Lancamento) query.uniqueResult();
	}
	
	private Lancamento recuperarRecolhimento(
			ProdutoEdicao produtoEdicaoRecolhimento, Date dataRecolhimentoLancamento) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT lcto FROM Lancamento lcto ");
		sql.append("      JOIN FETCH lcto.produtoEdicao pe ");
		sql.append("    WHERE pe = :produtoEdicao ");
		sql.append("      AND lcto.dataRecolhimentoPrevista = :dataRecolhimentoLancamento ");
		sql.append("      AND lcto.tipoLancamento = :tipoLancamento ");
		sql.append(" ORDER BY lcto.dataLancamentoDistribuidor ASC");
		
		Query query = getSession().createQuery(sql.toString());
		
		query.setParameter("produtoEdicao", produtoEdicaoRecolhimento);
		query.setDate("dataRecolhimentoLancamento", dataRecolhimentoLancamento);
		query.setParameter("tipoLancamento", TipoLancamento.LANCAMENTO);
		
		query.setMaxResults(1);
		query.setFetchSize(1);
		
		return (Lancamento) query.uniqueResult();
	}

	private ProdutoEdicao atualizarProdutoEdicao(EMS0108Input input,
			Produto produto, ProdutoEdicao produtoEdicao) {
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "atualizarProdutoEdicao(1) " )
			);
		
		produto.setSlogan(input.getSloganProduto());
		
		produto.setFormaComercializacao(getFormaComercializacao(input.getFormaComercializacao()));
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "atualizarProdutoEdicao(2) " )
			);
		if(input.getPeriodicidade()!=null){ 
		  produto.setPeriodicidade(getPeriodicidade(Integer.parseInt(input.getPeriodicidade())));
		}else {
			ndsiLoggerFactory.getLogger().logError(
					messageAux,
					EventoExecucaoEnum.HIERARQUIA,
					String.format( "Atualizar Produto Edicão. Produto:"+produto.getCodigo()+" com Periodicidade nula." )
				);
		  produto.setPeriodicidade(getPeriodicidade(1));
		}
		/*
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "atualizarProdutoEdicao(3) " )
			);
		*/
		produto.setTributacaoFiscal(getTributacaoFiscal(input.getTributacaoFiscal()));
		
		/*
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "atualizarProdutoEdicao(4) " )
			);
		*/
		this.getSession().persist(produto);
		/*
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "atualizarProdutoEdicao(5) " )
			);
			*/
		if(input.getPEB()!=null){
		  produtoEdicao.setPeb(input.getPEB());
		}else {
			produtoEdicao.setPeb(1);
		}
		/*
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "atualizarProdutoEdicao(6) " )
			);
			*/
		if(input.getPacotePadrao()!=null){
		produtoEdicao.setPacotePadrao(input.getPacotePadrao());
		}else {
			produtoEdicao.setPacotePadrao(1);
		}

		/*
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "atualizarProdutoEdicao(7) " )
			);
		*/
		
		DescontoLogistica descontoLogistica;
		
		Fornecedor fornecedor = produto.getFornecedor();
		
		Integer tipoDesconto = input.getTipoDesconto();
		
		if(tipoDesconto == null || tipoDesconto==0){
		 
		    tipoDesconto = 1;
		}
		
		descontoLogistica =
             descontoLogisticaRepository.obterDescontoLogisticaVigente(tipoDesconto,
                                                                       fornecedor.getId(),
                                                                       input.getDataMovimento());
		
		/*
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "atualizarProdutoEdicao(8) " )
			);
		*/
		
		if(input.getPercentualDesconto()!=null){
		  produtoEdicao.setDesconto(input.getPercentualDesconto());
		}else {
			produtoEdicao.setDesconto(new BigDecimal(1));
		}
		/*
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "atualizarProdutoEdicao(9) " )
			);
		*/
		produtoEdicao.setDescricaoDesconto(descontoLogistica.getDescricao());
		
		/*
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "atualizarProdutoEdicao(10) " )
			);
		*/
		this.getSession().persist(produtoEdicao);
		
		return produtoEdicao;
		
	}

	
	private ProdutoEdicao inserirProdutoEdicao(EMS0108Input input, Produto produto) {

		// Teste aaa
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "inserirProduto edicao" )
			);
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "1:"+input.getSloganProduto()+ 
						       "2:"+input.getFormaComercializacao() +
						       "3:"+input.getPeriodicidade() +
						       "4:"+input.getTributacaoFiscal() +
						       
						       "5:"+input.getEdicaoLancamento() 
						       
						       )
			);
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "6:"+input.getPesoProduto() +
						       "7:"+input.getCodigoBarrasFisicoProduto() 
						       )
			);
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 

						       "8:"+produto.getPeb() +
						       "9:"+input.getPEB() 
						       
						       )
			);
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
			
						       "10:"+input.getPacotePadrao() +
						       "11:"+input.getPercentualDesconto() 
						       )
			);
		
		if(input.getTipoDesconto()!=null) {
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "12:"+input.getTipoDesconto().toString() 
						       )
			);
		}
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "31" 
						       )
			);
		produto.setSlogan(input.getSloganProduto());
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "32" 
						       )
			);
		produto.setFormaComercializacao(getFormaComercializacao(input.getFormaComercializacao()));
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "33" 
						       )
			);
		if(input.getPeriodicidade()!=null){
		produto.setPeriodicidade(getPeriodicidade(Integer.parseInt(input.getPeriodicidade())));
		} else {
			produto.setPeriodicidade(getPeriodicidade(Integer.parseInt("1")));
		}
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "34" 
						       )
			);
		produto.setTributacaoFiscal(getTributacaoFiscal(input.getTributacaoFiscal()));
		
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "13" 
						       )
			);
		this.getSession().persist(produto);

		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "14" 
						       )
			);
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();

		produtoEdicao.setProduto(produto);
		produtoEdicao.setNumeroEdicao(input.getEdicaoLancamento());
		produtoEdicao.setPeso(input.getPesoProduto());
		produtoEdicao.setCodigoDeBarras(input.getCodigoBarrasFisicoProduto());
		//Tipo Classificação Default - ID Fixo.
		produtoEdicao.setTipoClassificacaoProduto(new TipoClassificacaoProduto(16L));
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "15" 
						       )
			);
		
		// setar default baseado no produto		
		produtoEdicao.setAtivo(true);
		produtoEdicao.setPacotePadrao(produto.getPacotePadrao());
		produtoEdicao.setPeb(produto.getPeb());
		produtoEdicao.setOrigem(Origem.MANUAL);

		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "16: "+ input.getPEB() 
						       )
			);
		//produtoEdicao.setCodigoDeBarraCorporativo(codigoBarrasCRP);
		//codigoEdicaoOrigem;
		
		if(input.getPEB()!=null){
		 produtoEdicao.setPeb(input.getPEB());
		}else {
			produtoEdicao.setPeb(1);
		}
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "161" 
						       )
			);
		if(input.getPacotePadrao()!=null){
		produtoEdicao.setPacotePadrao(input.getPacotePadrao());
		}else{
			produtoEdicao.setPacotePadrao(1);
				
		}
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "162" 
						       )
			);
		
		if(input.getPercentualDesconto()!=null){
		produtoEdicao.setDesconto(input.getPercentualDesconto());
		}else {
			produtoEdicao.setDesconto(new BigDecimal(1));
		}
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "17" 
						       )
			);
		
		if(input.getTipoDesconto()!=null) {
		produtoEdicao.setDescricaoDesconto(input.getTipoDesconto().toString());
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "18 if" 
						       )
			);
		}else {
			
			ndsiLoggerFactory.getLogger().logError(
					messageAux,
					EventoExecucaoEnum.HIERARQUIA,
					String.format( 
							       "18 else" 
							       )
				);
			produtoEdicao.setDescricaoDesconto("1");
		}

		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "19" 
						       )
			);
		this.getSession().persist(produtoEdicao);
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( 
						       "20" 
						       )
			);
		
		return produtoEdicao;
	}

	private void tratarParciais(Lancamento lancamento) {
		
		PeriodoLancamentoParcial periodoLancamentoParcial = lancamento.getPeriodoLancamentoParcial();
		
		if (periodoLancamentoParcial != null) {
			
			this.parciaisService.reajustarRedistribuicoes(
				periodoLancamentoParcial,
				lancamento.getDataLancamentoDistribuidor(),
				lancamento.getDataRecolhimentoDistribuidor());
		}
	}
	
	private ProdutoEdicao recuperarProdutoEdicao(String codigoPublicacao,
			Long edicao) {
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "recuperarProdutoEdicao(1)" )
			);
		
		// Obter o produto
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pe ");
		sql.append("FROM   ProdutoEdicao pe ");
		sql.append("	   JOIN FETCH pe.produto p ");
		sql.append("WHERE ");
		sql.append("	   pe.numeroEdicao = :numeroEdicao ");
		sql.append("	   AND p.codigo    = :codigoProduto ");

		Query query = getSession().createQuery(sql.toString());

		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "recuperarProdutoEdicao(2)" )
			);
		
		query.setParameter("numeroEdicao", edicao);
		query.setParameter("codigoProduto", codigoPublicacao);
		
		ndsiLoggerFactory.getLogger().logError(
				messageAux,
				EventoExecucaoEnum.HIERARQUIA,
				String.format( "recuperarProdutoEdicao(3)" )
			);
		
		query.setMaxResults(1);
		query.setFetchSize(1);

		return (ProdutoEdicao) query.uniqueResult();
	}

	private Produto recuperaProduto(String codigoPublicacao) {
		// Obter o produto
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT p ");
		sql.append("FROM   Produto p ");
		sql.append("WHERE ");
		sql.append("	  p.codigo    = :codigoProduto  ");

		Query query = getSession().createQuery(sql.toString());

		query.setParameter("codigoProduto", codigoPublicacao);
		query.setMaxResults(1);
		query.setFetchSize(1);

		return (Produto) query.uniqueResult();
	}

	private FormaComercializacao getFormaComercializacao(String formaComercializacao) {
		if ("2".equalsIgnoreCase(formaComercializacao)) {
			return FormaComercializacao.CONTA_FIRME;
		}
		return FormaComercializacao.CONSIGNADO;
	}
	
	private TributacaoFiscal getTributacaoFiscal(String codigoSituacaoTributaria) {
		if ("1".equalsIgnoreCase(codigoSituacaoTributaria)) {
			return TributacaoFiscal.TRIBUTADO;
		} else if ("2".equalsIgnoreCase(codigoSituacaoTributaria)) {
			return TributacaoFiscal.ISENTO;
		} else {
			return TributacaoFiscal.OUTROS;
		}
	}

	private PeriodicidadeProduto getPeriodicidade(int periodicidade) {
		return PeriodicidadeProduto.getByOrdem(periodicidade);
	}

	@Override
	public void posProcess(Object tempVar) {
		distribuidorService.desbloqueiaProcessosLancamentosEstudos();
	}
	
}