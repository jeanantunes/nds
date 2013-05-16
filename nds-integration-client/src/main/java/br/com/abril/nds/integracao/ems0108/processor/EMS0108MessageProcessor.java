package br.com.abril.nds.integracao.ems0108.processor;

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
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.DescontoLogisticaRepository;

@Component
public class EMS0108MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private DescontoLogisticaRepository descontoLogisticaRepository;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	private final static String DATA_ZEROS = "00000000";

	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	private EMS0108MessageProcessor() {

	}

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	/**
	 * Processa as linhas do arquivo da interface EMS0108
	 */
	@Override
	public void processMessage(Message message) {
		EMS0108Input input = (EMS0108Input) message.getBody();
		
		// Verifica se existe Produto		
		Produto produto = recuperaProduto(input.getCodigoPublicacao());
		if (null == produto) {
			ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.HIERARQUIA,
					String.format( "Produto %1$s não encontrado.", input.getCodigoPublicacao() )
				);
			return ;
		} /*else {
			if (!produto.getOrigem().equals(Origem.MANUAL)) {
				return;
			}
		}*/
		
		
		// regra para Registro de Lancamento 		
		regraLancamento(message, input, produto);
		
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
						String.format( "Produto %1$s Edicao %2$s não cadastrada.", input.getCodigoPublicacao(), input.getEdicaoRecolhimento().toString() )
					);
				return;
			} else {
				Lancamento lancamento = null;
				try {
					lancamento = this.recuperarRecolhimento(produtoEdicaoRecolhimento, DATE_FORMAT.parse(input.getDataLancamentoRecolhimentoProduto()));
				} catch (ParseException e1) {
					ndsiLoggerFactory.getLogger().logError(
							message,
							EventoExecucaoEnum.ERRO_INFRA,
							String.format( "Erro ao converter data %1$s Produto %2$s Edicao %3$s.", input.getDataLancamentoRecolhimentoProduto(), input.getCodigoPublicacao(), input.getEdicaoRecolhimento().toString() ));
					return;
				}
				if (null != lancamento) {
					
					if (lancamento.getStatus() == StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO || lancamento.getStatus() == StatusLancamento.BALANCEADO_RECOLHIMENTO) {
						ndsiLoggerFactory.getLogger().logWarning(
								message,
								EventoExecucaoEnum.ERRO_INFRA,
								String.format( "Registro não será atualizado pois já está em balanceamento / balanceado. Data de recolhimento: %1$s Produto: %2$s Edicao: %3$s.", input.getDataLancamentoRecolhimentoProduto(), input.getCodigoPublicacao(), input.getEdicaoRecolhimento().toString() ));
						return;
					}
					
					if (!lancamento.getDataRecolhimentoDistribuidor().equals(input.getDataLancamentoRecolhimentoProduto() )) {
					
						if (lancamento.getStatus().equals(StatusLancamento.BALANCEADO_RECOLHIMENTO)) {
							ndsiLoggerFactory.getLogger().logWarning(
									message,
									EventoExecucaoEnum.INF_DADO_ALTERADO,
									String.format( "Não foi possivel Alterar a data devido ao status de BALANCEADO_RECOLHIMENTO, para o Produto %1$s Edicao %2$s.", input.getCodigoPublicacao(), input.getEdicaoRecolhimento().toString())
								);
							return ;
						} else {
							if (input.getDataLancamentoRecolhimentoProduto() != null && input.getDataLancamentoRecolhimentoProduto().isEmpty() && !DATA_ZEROS.equals(input.getDataLancamentoRecolhimentoProduto())) {
								try {
									lancamento.setDataRecolhimentoDistribuidor(DATE_FORMAT.parse(input.getDataLancamentoRecolhimentoProduto()));
								} catch (ParseException e) {
									ndsiLoggerFactory.getLogger().logError(
											message,
											EventoExecucaoEnum.ERRO_INFRA,
											String.format( "Erro ao converter data %1$s Produto %2$s Edicao %3$s.", input.getDataLancamentoRecolhimentoProduto(), input.getCodigoPublicacao(), input.getEdicaoRecolhimento().toString() ));
									return;
								}
							}
							this.getSession().merge(lancamento);
						}
					}
				} else {
					ndsiLoggerFactory.getLogger().logError(
							message,
							EventoExecucaoEnum.RELACIONAMENTO,
							String.format( "Não existe recolhimento para o Produto %1$s Edicao %2$s. Na data de lancamento %3$s", input.getCodigoPublicacao(), input.getEdicaoRecolhimento().toString(), input.getDataLancamentoRecolhimentoProduto().toString() )
						);
				}

			}

		}
	}

	private void regraLancamento(Message message, EMS0108Input input,
			Produto produto) {
		if (!input.getEdicaoLancamento().equals(0L)) {
			ProdutoEdicao produtoEdicaoLancamento = this.recuperarProdutoEdicao(input.getCodigoPublicacao(), input.getEdicaoLancamento());		
			if (null == produtoEdicaoLancamento) {
				produtoEdicaoLancamento = inserirProdutoEdicao(input, produto);
				
				// no caso de inserir uma nova edicao atualiza o peso do produto
				produto.setPeso(input.getPesoProduto());
				this.getSession().merge(produto);
				
				ndsiLoggerFactory.getLogger().logWarning(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						String.format( "Produto %1$s Edicao %2$s cadastrada. Necessário atualizar o preço.", input.getCodigoPublicacao(), produtoEdicaoLancamento.getNumeroEdicao().toString() )
					);
			} else {
				produtoEdicaoLancamento = atualizarProdutoEdicao(input, produto, produtoEdicaoLancamento);
			}
			
			Lancamento lancamento;
			//try {
				//lancamento = this.recuperarLancamento(produtoEdicaoLancamento, DATE_FORMAT.parse(input.getDataLancamentoRecolhimentoProduto()));
				// Alterado em conjunto com Cesar Pop Punk para buscar o lançamento pela data de movimento
				lancamento = this.recuperarLancamento(produtoEdicaoLancamento, input.getDataMovimento());
			/*} catch (ParseException e1) {
				ndsiLoggerFactory.getLogger().logError(
						message,
						EventoExecucaoEnum.ERRO_INFRA,
						String.format( "Erro ao converter data %1$s Produto %2$s Edicao %3$s.", input.getDataLancamentoRecolhimentoProduto(), input.getCodigoPublicacao(), input.getEdicaoRecolhimento().toString() ));
				return;			}*/
			if (null == lancamento) {
				try {
					lancamento = inserirLancamento(produtoEdicaoLancamento, input);
				} catch (ParseException e) {
					ndsiLoggerFactory.getLogger().logError(
							message,
							EventoExecucaoEnum.ERRO_INFRA,
							String.format( "Erro ao converter data %1$s Produto %2$s Edicao %3$s.", input.getDataLancamentoRecolhimentoProduto(), input.getCodigoPublicacao(), input.getEdicaoRecolhimento().toString() ));
					return;
				}
				
				ndsiLoggerFactory.getLogger().logWarning(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						String.format( "Foi criado um lancamento para o Produto %1$s Edicao %2$s. Na data de lancamento %3$s", input.getCodigoPublicacao(), produtoEdicaoLancamento.getNumeroEdicao().toString(), input.getDataLancamentoRecolhimentoProduto().toString() )
					);				
			} else {
				if (!lancamento.getDataLancamentoDistribuidor().equals(input.getDataLancamentoRecolhimentoProduto())) {
					if (lancamento.getStatus().equals(StatusLancamento.BALANCEADO)) {
						ndsiLoggerFactory.getLogger().logWarning(
								message,
								EventoExecucaoEnum.INF_DADO_ALTERADO,
								String.format( "Não foi possivel Alterar a data devido ao status de BALANCEADO, para o Produto %1$s Edicao %2$s.", input.getCodigoPublicacao(), produtoEdicaoLancamento.getNumeroEdicao().toString())
							);	
						return ;
					} else {
						lancamento.setDataLancamentoDistribuidor(input.getDataMovimento());
						this.getSession().merge(lancamento);
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
		
		/*lancamento.setDataLancamentoDistribuidor(DATE_FORMAT.parse(input.getDataLancamentoRecolhimentoProduto()));
		lancamento.setDataLancamentoPrevista(DATE_FORMAT.parse(input.getDataLancamentoRecolhimentoProduto()));*/
		lancamento.setDataLancamentoDistribuidor(input.getDataMovimento());
		lancamento.setDataLancamentoPrevista(input.getDataMovimento());
		lancamento.setAlteradoInteface(true);
		lancamento.setStatus(StatusLancamento.CONFIRMADO);
		lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);

		int peb = produtoEdicaoLancamento.getPeb() == 0 ? produtoEdicaoLancamento.getProduto().getPeb() : produtoEdicaoLancamento.getPeb();
		if (peb == 0) {
			peb = 10;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(input.getDataMovimento());
		cal.add(Calendar.DATE, peb); 		
		//cal.add(Calendar.DATE, produtoEdicaoLancamento.getProduto().getPeb()); 		
		lancamento.setDataRecolhimentoDistribuidor(cal.getTime());
		lancamento.setDataRecolhimentoPrevista(cal.getTime());		

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
		sql.append(" ORDER BY lcto.dataLancamentoDistribuidor ASC");
		
		Query query = getSession().createQuery(sql.toString());
		
		query.setParameter("produtoEdicao", produtoEdicaoLancamento);
		query.setDate("dataMovimento", dataMovimento);
		
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
		sql.append("      AND lcto.dataLancamentoDistribuidor = :dataRecolhimentoLancamento ");
		sql.append(" ORDER BY lcto.dataLancamentoDistribuidor ASC");
		
		Query query = getSession().createQuery(sql.toString());
		
		query.setParameter("produtoEdicao", produtoEdicaoRecolhimento);
		query.setDate("dataRecolhimentoLancamento", dataRecolhimentoLancamento);
		
		query.setMaxResults(1);
		query.setFetchSize(1);
		
		return (Lancamento) query.uniqueResult();
	}

	private ProdutoEdicao atualizarProdutoEdicao(EMS0108Input input,
			Produto produto, ProdutoEdicao produtoEdicao) {
		
		produto.setSlogan(input.getSloganProduto());
		
		produto.setFormaComercializacao(getFormaComercializacao(input.getFormaComercializacao()));
		produto.setPeriodicidade(getPeriodicidade(Integer.parseInt(input.getPeriodicidade())));
		produto.setTributacaoFiscal(getTributacaoFiscal(input.getTributacaoFiscal()));
		
		this.getSession().persist(produto);
		
		produtoEdicao.setPeb(input.getPEB());
		produtoEdicao.setPacotePadrao(input.getPacotePadrao());

		DescontoLogistica descontoLogistica = descontoLogisticaRepository.obterPorTipoDesconto(input.getTipoDesconto());

		produtoEdicao.setDesconto(input.getPercentualDesconto());
		produtoEdicao.setDescricaoDesconto(descontoLogistica.getDescricao());

		this.getSession().persist(produtoEdicao);
		
		return produtoEdicao;
		
	}

	
	private ProdutoEdicao inserirProdutoEdicao(EMS0108Input input, Produto produto) {

		produto.setSlogan(input.getSloganProduto());
		
		produto.setFormaComercializacao(getFormaComercializacao(input.getFormaComercializacao()));
		produto.setPeriodicidade(getPeriodicidade(Integer.parseInt(input.getPeriodicidade())));
		produto.setTributacaoFiscal(getTributacaoFiscal(input.getTributacaoFiscal()));
		
		this.getSession().persist(produto);

		ProdutoEdicao produtoEdicao = new ProdutoEdicao();

		produtoEdicao.setProduto(produto);
		produtoEdicao.setNumeroEdicao(input.getEdicaoLancamento());
		produtoEdicao.setPeso(input.getPesoProduto());
		produtoEdicao.setCodigoDeBarras(input.getCodigoBarrasFisicoProduto());
		
		// setar default baseado no produto		
		produtoEdicao.setAtivo(true);
		produtoEdicao.setPacotePadrao(produto.getPacotePadrao());
		produtoEdicao.setPeb(produto.getPeb());
		produtoEdicao.setOrigem(Origem.MANUAL);

		//produtoEdicao.setCodigoDeBarraCorporativo(codigoBarrasCRP);
		//codigoEdicaoOrigem;
		
		produtoEdicao.setPeb(input.getPEB());
		produtoEdicao.setPacotePadrao(input.getPacotePadrao());
		produtoEdicao.setDesconto(input.getPercentualDesconto());
		produtoEdicao.setDescricaoDesconto(input.getTipoDesconto().toString());

		this.getSession().persist(produtoEdicao);
		
		return produtoEdicao;
	}

	private ProdutoEdicao recuperarProdutoEdicao(String codigoPublicacao,
			Long edicao) {
		
		// Obter o produto
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pe ");
		sql.append("FROM   ProdutoEdicao pe ");
		sql.append("	   JOIN FETCH pe.produto p ");
		sql.append("WHERE ");
		sql.append("	   pe.numeroEdicao = :numeroEdicao ");
		sql.append("	   AND p.codigo    = :codigoProduto ");

		Query query = getSession().createQuery(sql.toString());

		query.setParameter("numeroEdicao", edicao);
		query.setParameter("codigoProduto", codigoPublicacao);

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
		// TODO Auto-generated method stub
	}
	
}