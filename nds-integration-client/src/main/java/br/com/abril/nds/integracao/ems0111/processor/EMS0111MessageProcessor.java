package br.com.abril.nds.integracao.ems0111.processor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
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
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.integracao.DistribuidorService;

/**
 * @author Jones.Costa
 * @version 1.0
 */

@Component
public class EMS0111MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private Set<Date> datasBalanceaveis = new TreeSet<Date>();
	private Set<Date> datasNaoBalanceaveis = new TreeSet<Date>();
	private HashMap<String, Set> datas = new HashMap<String, Set>();
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ParciaisService parciaisService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		distribuidorService.bloqueiaProcessosLancamentosEstudos();
	}

	@Override
	public void processMessage(Message message) {
		
		//Recupera datas Abertas para matriz
		datas = lancamentoService.obterDiasMatrizLancamentoAbertos();
		datasBalanceaveis = datas.get("diasBalanceaveis");
		datasNaoBalanceaveis = datas.get("diasNaoBalanceaveis");
		
		EMS0111Input input = (EMS0111Input) message.getBody();
				
		// Validar Distribuidor:
		final String codDistribuidorSistema = message.getHeader().get(
				MessageHeaderProperties.CODIGO_DISTRIBUIDOR.name()).toString();
		
		final String codDistribuidorArquivo = input.getCodigoDistribuidor().toString();
		
		if (!codDistribuidorSistema.equals(codDistribuidorArquivo)) {
			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Distribuidor nao encontrato. Código: " 
					+ codDistribuidorArquivo);
			return;
		}
		
		
		// Validar Produto/Edicao
		final String codigoProduto = input.getCodigoProduto();
		final Long edicao = input.getEdicaoProduto();
		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoProduto,
				edicao);
		if (produtoEdicao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Impossivel realizar Insert/update - Nenhum resultado encontrado para Produto: "
							+ codigoProduto
							+ " e Edicao: " + edicao
							+ " na tabela produto_edicao");
			return;
		}
		
		
		// Verificação de alteração do Preço Previsto para o ProdutoEdiçao:
		final BigDecimal precoPrevistoAtual = this.tratarValorNulo(produtoEdicao.getPrecoPrevisto());
		final BigDecimal precoPrevistoCorrente = this.tratarValorNulo(input.getPrecoPrevisto());
		if (!precoPrevistoAtual.equals(precoPrevistoCorrente)) {
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteracao do Preco Previsto do Produto: "
							+ codigoProduto
							+ " e Edicao: " + edicao
							+ ", de: " + precoPrevistoAtual
							+ " para: " + precoPrevistoCorrente);
			produtoEdicao.setPrecoPrevisto(precoPrevistoCorrente);
			produtoEdicao.setPrecoVenda(precoPrevistoCorrente);
			this.getSession().merge(produtoEdicao);
		}
		
		
		if (input.getRepartePrevisto().equals(0L)) {
			return;
		}
		
		/**
		 * Modificado devido ser incoerente a realizar busca por um campo e persistir outro junto com a o Eduardo "PunkRock" Castro em 05/12
		 */
//		final Date dataGeracaoArquivo = input.getDataGeracaoArquivo();
		final Date dataGeracaoArquivo = input.getDataLancamento();

		Lancamento lancamento = this.getLancamentoPrevistoMaisProximo(
				produtoEdicao, dataGeracaoArquivo);
		if (lancamento == null ) {
			
			// Cadastrar novo lançamento
			lancamento = new Lancamento();
			
			// Cálcular data de recolhimento
			Calendar calRecolhimento = Calendar.getInstance();
			calRecolhimento.setTime(input.getDataLancamento());
			int peb = produtoEdicao.getPeb() == 0 ? produtoEdicao.getProduto().getPeb() : produtoEdicao.getPeb();
			if (peb == 0) {
				peb = 15;
			}
			calRecolhimento.add(Calendar.DAY_OF_MONTH, peb);
			final Date dataRecolhimento = calRecolhimento.getTime();

			// Data da Operação do sistema:
			final Date dataOperacao = distribuidorService.obter().getDataOperacao();
			lancamento.setDataStatus(dataOperacao);
			
			lancamento.setId(null);
			
			lancamento.setProdutoEdicao(produtoEdicao);
			
			lancamento.setDataLancamentoPrevista(input.getDataLancamento());
			
			lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
			
			lancamento.setReparte(BigInteger.valueOf(input.getRepartePrevisto()));
			
			lancamento.setStatus(
					(input.getRepartePrevisto() == 0) ? 
							StatusLancamento.CANCELADO : StatusLancamento.CONFIRMADO);
			
			lancamento.setRepartePromocional(BigInteger.valueOf(input.getRepartePromocional()));// confirmado
			
			lancamento.setDataCriacao(new Date());// confirmado
			
			try {
				lancamento.setDataLancamentoDistribuidor(getDiaMatrizAberta(input.getDataLancamento(),dataRecolhimento,message,codigoProduto,edicao));
			} catch (Exception e) {
				return;
			}
			
			lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);// confirmado
			
			lancamento.setDataRecolhimentoPrevista(dataRecolhimento);// confirmado 
			
			lancamento.setExpedicao(null);// default
			
			lancamento.setHistoricos(null);// default
			
			lancamento.setRecebimentos(null);// default
			
			lancamento.setSequenciaMatriz(null);// confirmado
			
			lancamento.setNumeroLancamento(1);

			// EFETIVAR INSERCAO NA BASE
			getSession().persist(lancamento);	
			
		} else {
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
			final BigInteger repartePrevisto = BigInteger.valueOf(
					input.getRepartePrevisto());
			
			if (null != lancamento.getReparte() && !lancamento.getReparte().equals(repartePrevisto)) {
				
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteracao do REPARTE PREVISTO do Produto: "
								+ codigoProduto + " e Edicao: " + edicao
								+ " , de: " + lancamento.getReparte() 
								+ "para: " + repartePrevisto);
				
				lancamento.setReparte(repartePrevisto);
			}
			
			final BigInteger repartePromocional = BigInteger.valueOf(
					input.getRepartePromocional());
			if (null != lancamento.getRepartePromocional() && !lancamento.getRepartePromocional().equals(repartePromocional)) {
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteracao do REPARTE PROMOCIONAL do Produto: "
								+ codigoProduto + " e Edicao: " + edicao
								+ " , de: " + lancamento.getRepartePromocional() 
								+ "para: " + repartePromocional);
				lancamento.setRepartePromocional(repartePromocional);
			}
			
			final Date dataLancamento = input.getDataLancamento();
			
			
			// Remover a hora, minuto, segundo e milissegundo para comparação:
			final Date dtLancamentoAtual = this.normalizarDataSemHora(
					lancamento.getDataLancamentoPrevista());
			Date dtLancamentoNovo = this.normalizarDataSemHora(dataLancamento);
			if (null != dtLancamentoAtual && !dtLancamentoAtual.equals(dtLancamentoNovo)) {
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteracao da DATA LANCAMENTO PREVISTO do Produto: "
								+ codigoProduto + " e Edicao: " + edicao
								+ " , de: " + simpleDateFormat.format(
										dtLancamentoAtual)
								+ "para: " + simpleDateFormat.format(
										dtLancamentoNovo));
				lancamento.setDataLancamentoPrevista(dtLancamentoNovo);
				
				boolean erroRetornoParciais =
					this.tratarParciais(lancamento, message, codigoProduto, edicao);
				
				if (erroRetornoParciais) {
					return;
				}
			}
			
			// Atualizar lançamento Distribuidor:
			final StatusLancamento status = (lancamento.getReparte().compareTo(BigInteger.ZERO) == 0) ? StatusLancamento.CANCELADO : lancamento.getStatus();
			
			boolean isStatusAlteracaoDataLancamento = ( (StatusLancamento.PLANEJADO.equals(status)) || (StatusLancamento.CONFIRMADO.equals(status)) ); 
			
			final Date dtLancamentoDistribuidor = this.normalizarDataSemHora(lancamento.getDataLancamentoDistribuidor());
			
			if (null != dtLancamentoDistribuidor && !dtLancamentoDistribuidor.equals(dtLancamentoNovo) && isStatusAlteracaoDataLancamento) {
				
				if(dtLancamentoNovo.before(distribuidorService.obterDataOperacaoDistribuidor())){
				
					try {
					 dtLancamentoNovo = getDiaMatrizAberta(dtLancamentoNovo, lancamento.getDataRecolhimentoDistribuidor(),message,codigoProduto,edicao);
				    } catch (Exception e) {
					  return;
				    }
				}
				// Alterado por solicitacao da trac 185
				
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteracao para PARCIAL da DATA LANCAMENTO DISTRIBUIDOR do Produto: "
								+ codigoProduto + " e Edicao: " + edicao
								+ " , de: " + simpleDateFormat.format(
										dtLancamentoNovo)
								+ "para: " + simpleDateFormat.format(
										dtLancamentoDistribuidor));
				
				lancamento.setDataLancamentoDistribuidor(dtLancamentoNovo);
				
				boolean erroRetornoParciais =
					this.tratarParciais(lancamento, message, codigoProduto, edicao);
				
				if (erroRetornoParciais) {
					return;
				}
			}
		}
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
					String.format("Erro ao processar as parcias para o Produto %1$s Edicao %2$s. " + e.getMessage(),
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
	private ProdutoEdicao obterProdutoEdicao(String codigoPublicacao,
			Long edicao) {

		try {

			Criteria criteria = this.getSession().createCriteria(
					ProdutoEdicao.class, "produtoEdicao");

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
	 * 
	 * @return
	 */
	private Lancamento getLancamentoPrevistoMaisProximo(
			ProdutoEdicao produtoEdicao, Date dataGeracaoArquivo) {
		
		Criteria criteria = this.getSession().createCriteria(Lancamento.class);

		criteria.add(Restrictions.ge("dataLancamentoPrevista", dataGeracaoArquivo));
		criteria.add(Restrictions.eq("produtoEdicao", produtoEdicao));
		criteria.add(Restrictions.eq("tipoLancamento", TipoLancamento.LANCAMENTO));
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
	
	private Date getDiaMatrizAberta(Date dataLctoDistibuidor,Date dataRecDistibuidor,Message message, String codigoProduto, Long edicao) throws Exception{
		
		
	    	
		if(datasNaoBalanceaveis==null || datasNaoBalanceaveis.isEmpty()){
		 return dataLctoDistibuidor;
		}else if(!datasNaoBalanceaveis.contains(dataLctoDistibuidor) && !dataLctoDistibuidor.before(distribuidorService.obterDataOperacaoDistribuidor())){
		 return dataLctoDistibuidor;
		}else{
			if(datasBalanceaveis!=null && !datasBalanceaveis.isEmpty()){
			 for(Date data : datasBalanceaveis){
				
				if(!data.before(dataLctoDistibuidor)){
			   
			        // usa calendar para subtrair data
			        Calendar calendarData = Calendar.getInstance();
			        calendarData.setTime(dataLctoDistibuidor);
			   
			        int numeroDiasParaSubtrair = 2;
			        // achar data de início
			        calendarData.add(Calendar.DATE,numeroDiasParaSubtrair);
			        
			        if(data.before(calendarData.getTime())){
			        	
			        	this.ndsiLoggerFactory.getLogger().logInfo(message,
								EventoExecucaoEnum.ERRO_INFRA,
								"Não foi inserido o lancamento do Produto: "
										+ codigoProduto
										+ " e Edicao: " + edicao
										+ ", data de Recolhimento: " + dataRecDistibuidor
										+ " para: " + data+ " é menor que 2 dias.");
			        	throw new Exception("Não foi inserido o lancamento do Produto: "
											+ codigoProduto
											+ " e Edicao: " + edicao
											+ ", data de Recolhimento: " + dataRecDistibuidor
											+ " para: " + data+ " é menor que 2 dias.");
			        }else{
					
			        	this.ndsiLoggerFactory.getLogger().logInfo(message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Alteracao da Data Lcto Distribuidor do Produto: "
									+ codigoProduto
									+ " e Edicao: " + edicao
									+ ", de: " + dataLctoDistibuidor
									+ " para: " + data);
					
			        	return data;
			        }
					
				}
				
			 }
		     return	(Date)datasBalanceaveis.toArray()[0];
			}else {
			  return dataLctoDistibuidor;
			}
		}
		
	}
	
}
