package br.com.abril.nds.integracao.ems0111.processor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0111Input;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DistribuicaoFornecedorService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;

/**
 * @author Jones.Costa
 * @version 1.0
 */

@Component
public class EMS0111MessageProcessor extends AbstractRepository implements MessageProcessor {
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ParciaisService parciaisService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	@Autowired
	private RecolhimentoService recolhimentoService;
	
	@Autowired
	private DistribuicaoFornecedorService distribuicaoFornecedorService;
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		
		distribuidorService.bloqueiaProcessosLancamentosEstudos();
	}

	@Override
	public void processMessage(Message message) {
		
		EMS0111Input input = (EMS0111Input) message.getBody();
				
		// Validar Distribuidor:
		final String codDistribuidorSistema = message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.name()).toString();
		
		final String codDistribuidorArquivo = input.getCodigoDistribuidor().toString();
		
		if (!new Integer(codDistribuidorSistema).equals(new Integer(codDistribuidorArquivo))) {
			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Distribuidor não encontrado. " 
					+ codDistribuidorArquivo);
			return;
		}
		
		// Validar Produto/Edicao
		final String codigoProduto = input.getCodigoProduto();
		final Long edicao = input.getEdicaoProduto();
		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoProduto, edicao);
		
		if (produtoEdicao == null) {
			
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Produto "
					+ codigoProduto
					+ " e Edição " + edicao
					+ " não encontrado.");
			return;
		} else {
			//Atualiza a PEB
			if (input.getDataRecolhimento() !=null && input.getDataRecolhimento().after(input.getDataLancamento())){
				produtoEdicao.setPeb(
				 new Long(TimeUnit.DAYS.convert(input.getDataRecolhimento().getTime()- input.getDataLancamento().getTime(),TimeUnit.MILLISECONDS)).intValue()	
				);
			}
		
		
		
		// Verificação de alteração do Preço Previsto para o ProdutoEdiçao:
		BigDecimal precoPrevistoAtual = this.tratarValorNulo(produtoEdicao.getPrecoPrevisto());
		BigDecimal precoPrevistoCorrente = this.tratarValorNulo(input.getPrecoPrevisto());
		
		precoPrevistoAtual = precoPrevistoAtual.setScale(4,RoundingMode.HALF_UP);
		precoPrevistoCorrente = precoPrevistoCorrente.setScale(4,RoundingMode.HALF_UP);
		
		
			if (precoPrevistoAtual.compareTo(precoPrevistoCorrente)!=0 && precoPrevistoCorrente.doubleValue()>0) {
				this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Preço Previsto/Venda"
							+ " de " + precoPrevistoAtual
							+ " para " + precoPrevistoCorrente
							+ " Produto "+codigoProduto
							+ " Edição " + edicao);
			
				produtoEdicao.setPrecoPrevisto(precoPrevistoCorrente);
				produtoEdicao.setPrecoCusto(precoPrevistoCorrente);
				produtoEdicao.setPrecoVenda(precoPrevistoCorrente);
				
			}
		}

		this.getSession().merge(produtoEdicao);
		/**
		 * Modificado devido ser incoerente a realizar busca por um campo e persistir outro junto com a o Eduardo "PunkRock" Castro em 05/12
		 */
		final Date dataGeracaoArquivo = input.getDataLancamento();

		StatusLancamento[] statusLancamento = new StatusLancamento[] {StatusLancamento.CONFIRMADO, StatusLancamento.PLANEJADO};

		Lancamento lancamento = this.getLancamentoPrevistoMaisProximo(produtoEdicao, dataGeracaoArquivo, statusLancamento);

		if(lancamento != null) {

			BigInteger repartePromocional = BigInteger.valueOf(input.getRepartePromocional());

			if (null != lancamento.getRepartePromocional() && !lancamento.getRepartePromocional().equals(repartePromocional)) {
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteração do REPARTE PROMOCIONAL"
								+ " de " + lancamento.getRepartePromocional() 
								+ " para " + repartePromocional
								+ " Produto "+codigoProduto
								+ " Edição " + edicao);
				lancamento.setRepartePromocional(repartePromocional);
				
				
				
			}
			
			Date dataOriginal = input.getDataLancamento();
			Date dataSugerida = lancamentoService.obterDataLancamentoValido(dataOriginal, produtoEdicao.getProduto().getFornecedor().getId());
			final Date dataRecolhimento = this.atualizaPeb(input.getDataLancamento(), produtoEdicao);
			
			final boolean produtoContaFirme = (FormaComercializacao.CONTA_FIRME.equals(produtoEdicao.getProduto().getFormaComercializacao()));
			
			if(produtoContaFirme) {
				
				lancamento.setDataRecolhimentoDistribuidor(dataSugerida);// confirmado
				
				lancamento.setDataRecolhimentoPrevista(dataOriginal);// confirmado 
			} else {
				
				Date dataRecolhimentoSugerida = recolhimentoService.obterDataRecolhimentoValido(this.getProximaDataUtil(dataRecolhimento, produtoEdicao.getProduto().getFornecedor().getId(), OperacaoDistribuidor.RECOLHIMENTO),produtoEdicao.getProduto().getFornecedor().getId());

				//lancamento.setDataRecolhimentoDistribuidor(dataRecolhimentoSugerida);// confirmado
				lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);// confirmado
				
				lancamento.setDataRecolhimentoPrevista(dataRecolhimento);// confirmado 
			}

		}
		
		if (lancamento == null) {
		    
			// Cadastrar novo lançamento
			lancamento = new Lancamento();
			
			final Date dataRecolhimento = this.atualizaPeb(input.getDataLancamento(), produtoEdicao);

			// Data da Operação do sistema:
			final Date dataOperacao = distribuidorService.obter().getDataOperacao();
			lancamento.setDataStatus(dataOperacao);
			
			lancamento.setId(null);
			
			lancamento.setProdutoEdicao(produtoEdicao);
			
			lancamento.setDataLancamentoPrevista(input.getDataLancamento());
			
			lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
			
			lancamento.setReparte(BigInteger.valueOf(input.getRepartePrevisto()));
			
			lancamento.setStatus((input.getRepartePrevisto() == 0) ? StatusLancamento.CANCELADO : StatusLancamento.CONFIRMADO);
			
			lancamento.setRepartePromocional(BigInteger.valueOf(input.getRepartePromocional()));// confirmado
			
			lancamento.setDataCriacao(new Date());// confirmado
			
			
			try {

				Date dataOriginal = input.getDataLancamento();
				Date dataSugerida = lancamentoService.obterDataLancamentoValido(dataOriginal, produtoEdicao.getProduto().getFornecedor().getId());

				if(dataOriginal.compareTo(dataSugerida) != 0) {

					this.ndsiLoggerFactory.getLogger().logWarning(message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Alteração da Data Lcto Distribuidor"
									+ " de  " + DateUtil.formatarDataPTBR(dataOriginal)
									+ " para  " + DateUtil.formatarDataPTBR(dataSugerida)
									+ " Produto "+codigoProduto
									+ " Edição " + edicao);


				}

				lancamento.setDataLancamentoDistribuidor(dataSugerida);
				final boolean produtoContaFirme = (FormaComercializacao.CONTA_FIRME.equals(produtoEdicao.getProduto().getFormaComercializacao()));
				
				if(produtoContaFirme) {
					
					lancamento.setDataRecolhimentoDistribuidor(dataSugerida);// confirmado
					
					lancamento.setDataRecolhimentoPrevista(dataOriginal);// confirmado 
				} else {
					
					Date dataRecolhimentoSugerida = recolhimentoService.obterDataRecolhimentoValido(this.getProximaDataUtil(dataRecolhimento, produtoEdicao.getProduto().getFornecedor().getId(), OperacaoDistribuidor.RECOLHIMENTO),produtoEdicao.getProduto().getFornecedor().getId());

					//lancamento.setDataRecolhimentoDistribuidor(dataRecolhimentoSugerida);// confirmado
					lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);// confirmado
					
					lancamento.setDataRecolhimentoPrevista(dataRecolhimento);// confirmado 
				}


			} catch (Exception e) {
				
				return;
			}
			
			lancamento.setExpedicao(null);// default
			
			lancamento.setHistoricos(null);// default
			
			lancamento.setRecebimentos(null);// default
			
			lancamento.setSequenciaMatriz(null);// confirmado
			
			lancamento.setNumeroLancamento(1);

			// EFETIVAR INSERCAO NA BASE
			getSession().persist(lancamento);	
			
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Lancamento Inserido "
							+ " Lancamento "+simpleDateFormat.format(lancamento.getDataLancamentoDistribuidor())
							+ " Recolhimento "+simpleDateFormat.format(lancamento.getDataRecolhimentoDistribuidor())
							+ " Produto "+codigoProduto
							+ " Edição " + edicao);
			
			return;
			
		} else if(lancamento.getStatus().equals(StatusLancamento.CONFIRMADO) 
			   || lancamento.getStatus().equals(StatusLancamento.PLANEJADO)
			   || lancamento.getStatus().equals(StatusLancamento.BALANCEADO)
			   || lancamento.getStatus().equals(StatusLancamento.EM_BALANCEAMENTO)
			   || lancamento.getStatus().equals(StatusLancamento.FURO)){
		    
		    boolean alterarData;
		    
		    if (!lancamento.getStatus().equals(StatusLancamento.CONFIRMADO)
		            && !lancamento.getStatus().equals(StatusLancamento.PLANEJADO)) {
		        
		        alterarData = false;
		        
		    } else {
		        
		        alterarData = true;
		    }
		    
			lancamento.setAlteradoInteface(true);
			/*
			 * Atualizar Lançamento:
			 * 
			 * 01) Atualizar os atributos (e 'logar'):
			 * - Reparte Previsto; Reparte Promocional; Tipo Lançamento; 
			 * Data Lançamento Previsto;
			 * 
			 * 02) Se da Data Lançamento Distribuidor for diferente da Data de 
			 * Geração do Arquivo e o Status for diferente de Balanceado ou 
			 * Balanceado Lançamento, realizar o log e alterar a data 
			 * Lançamento Distribuidor;
			 */
			final BigInteger repartePrevisto = BigInteger.valueOf(input.getRepartePrevisto());
			
			if (null != lancamento.getReparte() && !lancamento.getReparte().equals(repartePrevisto)) {
				
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteração do REPARTE PREVISTO"
								+ " de " + lancamento.getReparte() 
								+ " para " + repartePrevisto
								+ " Produto "+codigoProduto
								+ " Edição " + edicao);
				
				lancamento.setReparte(repartePrevisto);
			}
			
			lancamento.setDataLancamentoPrevista(input.getDataLancamento());
			final BigInteger repartePromocional = BigInteger.valueOf(input.getRepartePromocional());
			
			if (null != lancamento.getRepartePromocional() && !lancamento.getRepartePromocional().equals(repartePromocional)) {
				
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteração do REPARTE PROMOCIONAL"
								+ " de " + lancamento.getRepartePromocional() 
								+ " para " + repartePromocional
								+ " Produto "+codigoProduto
								+ " Edição " + edicao);
				lancamento.setRepartePromocional(repartePromocional);
			}
			
			if (alterarData) {
			
    			final Date dataLancamento = input.getDataLancamento();
    			
    			// Remover a hora, minuto, segundo e milissegundo para comparação:
    			final Date dtLancamentoAtual = this.normalizarDataSemHora(lancamento.getDataLancamentoPrevista());
    			Date dtLancamentoNovo = lancamentoService.obterDataLancamentoValido(this.normalizarDataSemHora(dataLancamento), new Long(produtoEdicao.getProduto().getFornecedor().getId()));
    			if (null != dtLancamentoAtual && !dtLancamentoAtual.equals(dtLancamentoNovo)) {
    				
    				
    				
    				boolean erroRetornoParciais = this.tratarParciais(lancamento, message, codigoProduto, edicao);
    				
    				if (erroRetornoParciais) {
    					
    					return;
    				}
    				
    			}
    			
				
				
    			// Atualizar lançamento Distribuidor:
    			final StatusLancamento status = (lancamento.getReparte().compareTo(BigInteger.ZERO) == 0) ? StatusLancamento.CANCELADO : lancamento.getStatus();
    			
    			boolean isStatusAlteracaoDataLancamento = ( (StatusLancamento.PLANEJADO.equals(status)) || (StatusLancamento.CONFIRMADO.equals(status)) || (StatusLancamento.FURO.equals(status)) ); 
    			
    			final Date dtLancamentoDistribuidor = this.normalizarDataSemHora(lancamento.getDataLancamentoDistribuidor());
    			
    			if (null != dtLancamentoDistribuidor && !	dtLancamentoDistribuidor.equals(dtLancamentoNovo) && isStatusAlteracaoDataLancamento) {

    				try { 
    					
    					dtLancamentoNovo = lancamentoService.obterDataLancamentoValido(input.getDataLancamento(), produtoEdicao.getProduto().getFornecedor().getId());
    				} catch (Exception e) {
    					
    					return;
    				}

    				if(!simpleDateFormat.format(dtLancamentoNovo).equals(simpleDateFormat.format(dtLancamentoDistribuidor))){
    				
    					this.ndsiLoggerFactory.getLogger().logInfo(message,
    						EventoExecucaoEnum.INF_DADO_ALTERADO,
    						"Alteração da DATA LANCAMENTO DISTRIBUIDOR"						
    						+ " de " + simpleDateFormat.format(dtLancamentoDistribuidor)
    								+ " para " + DateUtil.formatarDataPTBR(dtLancamentoNovo)
    								+ " Produto "+codigoProduto
    								+ " Edição " + edicao);
    				}
    				
    				lancamento.setDataLancamentoDistribuidor(dtLancamentoNovo);
    				
    				boolean erroRetornoParciais = this.tratarParciais(lancamento, message, codigoProduto, edicao);
    				
    				if(erroRetornoParciais) {
    					
    					return;
    				}
    			}
			}
		}
		Date dataRecolhimento = this.atualizaPeb(input.getDataLancamento(), produtoEdicao);
		
		if(lancamento.getDataRecolhimentoDistribuidor() != null) {
			
			this.ndsiLoggerFactory.getLogger().logInfo(message,
				EventoExecucaoEnum.INF_DADO_ALTERADO,
				"Alteração da DATA RECOLHIMENTO PREVISTO/DISTRIBUIDOR"
						+ " de " + DateUtil.formatarDataPTBR(lancamento.getDataRecolhimentoDistribuidor())
						+ " para " + DateUtil.formatarDataPTBR(dataRecolhimento)
						+ " Produto "+codigoProduto
						+ " Edição " + edicao);
		// } else {
			
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração da DATA RECOLHIMENTO PREVISTO/DISTRIBUIDOR"
							+ " de Nulo"
							+ " para " + DateUtil.formatarDataPTBR(dataRecolhimento)
							+ " Produto "+codigoProduto
							+ " Edição " + edicao);
		}
		
		lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);// confirmado
		
		lancamento.setDataRecolhimentoPrevista(dataRecolhimento);// confirmado 
		this.getSession().merge(lancamento);
	}
	
	private boolean tratarParciais(Lancamento lancamento, Message message, String codigoProduto, Long edicao) {
		
		try {
			
			PeriodoLancamentoParcial periodoLancamentoParcial = lancamento.getPeriodoLancamentoParcial();
			
			if (periodoLancamentoParcial != null) {
				
				this.parciaisService.reajustarRedistribuicoes(
					periodoLancamentoParcial,
					lancamento.getDataLancamentoDistribuidor(),
					lancamento.getDataRecolhimentoDistribuidor());
			}
		
			return false;
			
		} catch (Exception e) {
			ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					String.format("Erro ao processar as parcias para o Produto %1$s Edição %2$s. " + e.getMessage(),
								  codigoProduto, edicao));
			
			return true;
		}
	}
	
	/**
	 * Obtém o Produto Edição cadastrado previamente.
	 * 
	 * @param codigoPublicacao Código da Publicação.
	 * @param edicao Número da Edição.
	 * 
	 * @return
	 */
	private ProdutoEdicao obterProdutoEdicao(String codigoPublicacao, Long edicao) {

		try {

			Criteria criteria = this.getSession().createCriteria(ProdutoEdicao.class, "produtoEdicao");

			criteria.createAlias("produtoEdicao.produto", "produto");
			criteria.setFetchMode("produto", FetchMode.JOIN);

			criteria.add(Restrictions.eq("produto.codigo", codigoPublicacao));
			criteria.add(Restrictions.eq("produtoEdicao.numeroEdicao", edicao));

			return (ProdutoEdicao) criteria.uniqueResult();

		} catch (Exception e) {
			
			throw new RuntimeException(e);
		}
	}	
		
	/**
	 * Obtém o Lançamento com a "Data de lançamento Previsto" mais próximo da 
	 * "Data de Geração do Arquivo".
	 *  
	 * @param produtoEdicao
	 * @param dataGeracaoArquivo Data de Geração do Arquivo.
	 * @param statusLancamento
	 * 
	 * @return
	 */
	private Lancamento getLancamentoPrevistoMaisProximo(ProdutoEdicao produtoEdicao, Date dataGeracaoArquivo, StatusLancamento[] statusLancamento) {
		
		Criteria criteria = this.getSession().createCriteria(Lancamento.class);

		criteria.add(Restrictions.eq("produtoEdicao", produtoEdicao));
		
		criteria.addOrder(Order.asc("dataLancamentoPrevista"));
		
		criteria.setFetchSize(1);
		criteria.setMaxResults(1);
		
		return (Lancamento) criteria.uniqueResult();
	}
	/**
	 * Trata o valor de um Number para evitar nullpointer.<br>
	 * Caso o valor do Number seja null, será retornado 0 (zero).
	 * @param valor
	 * @return
	 */
	private BigDecimal tratarValorNulo(BigDecimal valor) {
		
		return valor == null ? BigDecimal.ZERO : valor;
	}
		
	/**
	 * Normaliza uma data, para comparações, zerando os valores de hora (hora,
	 * minuto, segundo e milissendo).
	 * 
	 * @param dt
	 * 
	 * @return
	 */
	private Date normalizarDataSemHora(Date dt) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
	}
	
	@Override
	public void posProcess(Object tempVar) {
		
		distribuidorService.desbloqueiaProcessosLancamentosEstudos();
	}
	
	private Date atualizaPeb(Date dataLancamento, ProdutoEdicao produtoEdicao) {
		
		// Cálcular data de recolhimento
		Calendar calRecolhimento = Calendar.getInstance();
		calRecolhimento.setTime(dataLancamento);
		int peb = produtoEdicao.getPeb() == 0 ? produtoEdicao.getProduto().getPeb() : produtoEdicao.getPeb();
		if (peb <= 0) {
			peb = 15;
		}
		calRecolhimento.add(Calendar.DAY_OF_MONTH, peb);
		
		while(!calendarioService.isDiaOperante(calRecolhimento.getTime(), produtoEdicao.getProduto().getFornecedor().getId(), OperacaoDistribuidor.RECOLHIMENTO)) {
			
			calRecolhimento.add(Calendar.DAY_OF_MONTH, 1);
		};
		
		return calRecolhimento.getTime();
	}
	
private Date getProximaDataUtil(Date data, Long idFornecedor, OperacaoDistribuidor operacaoDistribuidor) {
		
		Date novaData = this.parciaisService.obterDataUtilMaisProxima(data);

		boolean hasMatrizConfirmada = this.lancamentoService.existeMatrizRecolhimentoConfirmado(data);
		boolean fornecedorOpera = this.distribuicaoFornecedorService.obterCodigosDiaDistribuicaoFornecedor(idFornecedor, operacaoDistribuidor)
				.contains(DateUtil.toCalendar(novaData).get(Calendar.DAY_OF_WEEK));

		if (!hasMatrizConfirmada && fornecedorOpera) {

			return novaData;
		}
		
		return this.getProximaDataUtil(DateUtil.adicionarDias(novaData, 1), idFornecedor, operacaoDistribuidor);

	}
	
}