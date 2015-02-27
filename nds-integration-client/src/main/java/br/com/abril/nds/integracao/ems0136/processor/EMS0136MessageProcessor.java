package br.com.abril.nds.integracao.ems0136.processor;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0136Input;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.PeriodoLancamentoParcialRepository;
import br.com.abril.nds.service.DistribuicaoFornecedorService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;

import com.google.common.collect.ImmutableList;

@Component
public class EMS0136MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private PeriodoLancamentoParcialRepository periodoLancamentoParcialRepository;

	@Autowired
	private ParciaisService parciaisService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private DistribuicaoFornecedorService distribuicaoFornecedorService;

	private static final int PEB_MINIMA = 10;

	private static final ImmutableList<StatusLancamento> LANCAMENTO_EXPEDIDO = 
			ImmutableList.of(
				StatusLancamento.FURO, 
				StatusLancamento.BALANCEADO, 
				StatusLancamento.EM_BALANCEAMENTO, 
				StatusLancamento.EXPEDIDO
			);

	private static final ImmutableList<StatusLancamento> LANCAMENTO_ABERTO = 
			ImmutableList.of(
				StatusLancamento.PLANEJADO,
				StatusLancamento.CONFIRMADO
			);

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {}

	@Override
	public void processMessage(Message message) {
		
		EMS0136Input input = (EMS0136Input) message.getBody();
		
		if(!this.validarCodigoDistribuidor(message, input)){
			return;
		}

		ProdutoEdicao produtoEdicao = this.obterProdutoEdicaoValidado(message,input);
		
		if(produtoEdicao == null){
			return;
		}

		this.tratarDatasInput(input, produtoEdicao,message);

		LancamentoHelper helper = this.tratarLancamentosExistentes(produtoEdicao, input);
		
		if (helper.getLancamentosManter().isEmpty() && !this.validarDatasLancamento(input.getDataLancamento(), input.getDataRecolhimento())) {
			
			return;
		}

		LancamentoParcial lancamentoParcial = this.tratarLancamentoParciall(input,produtoEdicao);
		
		PeriodoLancamentoParcial periodo = this.tratarPeriodo(lancamentoParcial, input);
		
		Lancamento lancamento = this.tratarLancamento(produtoEdicao, periodo, helper, input);
		
		this.remove(helper.getLancamentosRemover());

		this.merge(lancamentoParcial, periodo, lancamento, produtoEdicao);
	}
	
	private boolean  validarCodigoDistribuidor(Message message, EMS0136Input input) {
		
		if(!this.distribuidorService.isDistribuidor(Integer.valueOf(input.getCodigoDistribuidor()))) {
			
			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.RELACIONAMENTO, 
					"Código do distribuidor do arquivo não é o mesmo do Sistema.");
			return false;
		}
		
		return true;
	}

	private ProdutoEdicao obterProdutoEdicaoValidado(Message message,EMS0136Input input) {
		
		String codigoProduto = input.getCodigoProduto();
		Long numeroEdicao = input.getEdicaoCapa();
		
		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoProduto,numeroEdicao);
	
		if (produtoEdicao == null) {
			
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Produto "
					+ codigoProduto + " Edição " + numeroEdicao
					+" não encontrado.");
			return null;
		}
		
		produtoEdicao.setParcial(true);

		return produtoEdicao;
	}
	
	private ProdutoEdicao obterProdutoEdicao(String codigoProduto,Long numeroEdicao) {
		
		try {

			Criteria criteria = this.getSession().createCriteria(ProdutoEdicao.class, "produtoEdicao");

			criteria.createAlias("produtoEdicao.produto", "produto");
			criteria.setFetchMode("produto", FetchMode.JOIN);

			criteria.add(Restrictions.eq("produto.codigo", codigoProduto));
			criteria.add(Restrictions.eq("produtoEdicao.numeroEdicao", numeroEdicao));

			return (ProdutoEdicao) criteria.uniqueResult();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Realiza o tratamento de lançamentos já existentes para o produto edição a ser inserido via interface.<br />
	 * O tratamento consiste em avaliar se o produto edição em questão já é uma parcial. 
	 * Caso este seja, nada será alterado em seus lançamentos, caso contrário será validado se existe lançamnetos expedidos 
	 * ({@link StatusLancamento#EXPEDIDO}, estes terão a data de recolhimento ajustada, apenas), 
	 * já se houverem apenas lançamentos abertos 
	 * ({@link StatusLancamento#PLANEJADO} / {@link StatusLancamento#CONFIRMADO}) os mesmos serão deletados.   
	 * 
	 * @param produtoEdicao - Edição a ser inserida como parcial.
	 * @param dataRecolhimento - Recolhimento útil, vindo do arquivo.
	 * @return Lançamentos que serão removidos, posteriormente.
	 */
	private LancamentoHelper tratarLancamentosExistentes(ProdutoEdicao produtoEdicao, EMS0136Input input) {
		
		LancamentoHelper helper = new LancamentoHelper();

		LancamentoParcial lp = produtoEdicao.getLancamentoParcial();

		if (lp != null && input.getNumeroPeriodo() > lp.getPeriodoFinal().getNumeroPeriodo()) {

			return helper;
		}

		Set<Lancamento> lancamentosDaEdicao = produtoEdicao.getLancamentos();

		for (Lancamento lancamentoAtual : lancamentosDaEdicao) {

			if (LANCAMENTO_EXPEDIDO.contains(lancamentoAtual.getStatus())) {
				
				int numeroLancamento = 1;
				
				if (lancamentoAtual.getPeriodoLancamentoParcial() != null) {
					numeroLancamento = lancamentoAtual.getPeriodoLancamentoParcial().getPrimeiroLancamento().getNumeroLancamento() + 1; 
				}

				lancamentoAtual.setNumeroLancamento(numeroLancamento);
				lancamentoAtual.setDataRecolhimentoPrevista(input.getDataRecolhimento());
				lancamentoAtual.setDataRecolhimentoDistribuidor(input.getDataRecolhimento());

				helper.addLancamentoManter(lancamentoAtual);

			} else if (LANCAMENTO_ABERTO.contains(lancamentoAtual.getStatus())) {
				helper.addLancamentosRemover(lancamentoAtual);
			} else {
				helper.addLancamentoManter(lancamentoAtual);
			}
		}
		
		//if (lp != null) {

			//for (Lancamento lancamentoRemover : helper.getLancamentosRemover()) {
				
				//lp.getPeriodos().remove(lancamentoRemover.getPeriodoLancamentoParcial());
			//}
			
			//lancamentosDaEdicao.removeAll(helper.getLancamentosRemover());
		//}

		return helper;
	}

	/**
	 * Obtém um lançamento parcial referente ao produto edição a ser tratado na interface.
	 * Caso exista uma parcial, será retornada da base de dados, caso contrário será criado um novo objeto com base nos dados do arquivo.
	 * 
	 * @param input
	 * @param produtoEdicao
	 * @return
	 */
	private LancamentoParcial tratarLancamentoParciall(EMS0136Input input,ProdutoEdicao produtoEdicao) {
		
		LancamentoParcial lancamentoParcial = produtoEdicao.getLancamentoParcial(); //this.consultarLancalmentoParcial(produtoEdicao);
		
		if(lancamentoParcial == null){
			
			lancamentoParcial = this.criarNovoLancamentoParcial(produtoEdicao, input, input.getDataLancamento(), input.getDataRecolhimento());			
		}
		
		lancamentoParcial.setRecolhimentoFinal(input.getDataRecolhimento());
		
		return lancamentoParcial;
	}

	private LancamentoParcial criarNovoLancamentoParcial(ProdutoEdicao produtoEdicao, EMS0136Input input, Date dataLancamento, Date dataRecolhimento){

		LancamentoParcial parcial = new LancamentoParcial();
		
		parcial.setProdutoEdicao(produtoEdicao);
		
		StatusLancamentoParcial status = this.obterStatusLancamentoParcial(input);
		
		parcial.setStatus(status);
		
		parcial.setLancamentoInicial(dataLancamento);
		
		parcial.setRecolhimentoFinal(dataRecolhimento);
		
		return parcial;
	}

	private StatusLancamentoParcial obterStatusLancamentoParcial(EMS0136Input input) {
		/*
		 * Se a "Data de Operação" > "Data de Recolhimento" então o status é
		 * RECOLHIDO, senão, é PROJETADO
		 */
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		StatusLancamentoParcial status = dataOperacao.after(
			input.getDataRecolhimento())
				? StatusLancamentoParcial.RECOLHIDO
				: StatusLancamentoParcial.PROJETADO;
		return status;
	}

	private void tratarDatasInput(EMS0136Input input, ProdutoEdicao produtoEdicao,Message message) {


		Date dataOriginal = input.getDataLancamento();
		Date dataSugerida = lancamentoService.obterDataLancamentoValido(dataOriginal,produtoEdicao.getProduto().getFornecedor().getId());
		
		
		Date dataLancamento = this.getProximaDataUtil(dataSugerida, produtoEdicao.getProduto().getFornecedor().getId(), OperacaoDistribuidor.DISTRIBUICAO);
		dataLancamento = lancamentoService.obterDataLancamentoValido(dataLancamento,produtoEdicao.getProduto().getFornecedor().getId());
		
		Date dataRecolhimento = this.getProximaDataUtil(input.getDataRecolhimento(), produtoEdicao.getProduto().getFornecedor().getId(), OperacaoDistribuidor.RECOLHIMENTO);
		
		if(dataOriginal.compareTo(dataLancamento)!=0){
			
			 this.ndsiLoggerFactory.getLogger().logWarning(message,
			 		 EventoExecucaoEnum.INF_DADO_ALTERADO,
					 "Alteração da Data Lcto Distribuidor"
							+ " de  " + DateUtil.formatarDataPTBR(dataOriginal)
							+ " para  " + DateUtil.formatarDataPTBR(dataSugerida)
							+ " Produto "+produtoEdicao.getProduto().getCodigo()
							+ " Edição " + produtoEdicao.getNumeroEdicao());
			
			 
		}
		
		input.setDataLancamento(dataLancamento);
		input.setDataRecolhimento(dataRecolhimento);
	}
	
	private boolean validarDatasLancamento(Date dataLancamento, Date dataRecolhimento) {
		
		if (DateUtil.obterDiferencaDias(dataLancamento, dataRecolhimento) < PEB_MINIMA) {
			
			return false;

		} else if (DateUtil.isDataInicialMaiorDataFinal(this.distribuidorService.obterDataOperacaoDistribuidor(), dataRecolhimento)) {

			return false;
		}

		return true;
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

	private PeriodoLancamentoParcial tratarPeriodo(LancamentoParcial parcial, EMS0136Input input) {
		
		Date dataRecolhimento = input.getDataRecolhimento();
		
		if (parcial.getPeriodos() == null || parcial.getPeriodos().isEmpty()) {

			return this.criarNovoPeriodoLancamento(parcial, 1, TipoLancamentoParcial.FINAL, dataRecolhimento);
		}

		PeriodoLancamentoParcial periodo = parcial.getPeriodoPorNumero(input.getNumeroPeriodo()); 

		PeriodoLancamentoParcial ultimoPeriodo = parcial.getPeriodoFinal();

		if (ultimoPeriodo != null && (ultimoPeriodo.equals(periodo) || ultimoPeriodo.after(periodo))) {

			return periodo;
		} 

		ultimoPeriodo.setTipo(TipoLancamentoParcial.PARCIAL);

		if (periodo == null) {
			
			Integer numeroNovoPeriodo = ultimoPeriodo.getNumeroPeriodo() + 1;
			
			return this.criarNovoPeriodoLancamento(parcial, numeroNovoPeriodo, TipoLancamentoParcial.FINAL, dataRecolhimento);
		}
		
		periodo.setNumeroPeriodo(ultimoPeriodo.getNumeroPeriodo()+1);
		periodo.setTipo(TipoLancamentoParcial.FINAL);
		
		return periodo;
	}

	private PeriodoLancamentoParcial criarNovoPeriodoLancamento(
				LancamentoParcial lancamentoParcial, 
				Integer numeroPeriodo, 
				TipoLancamentoParcial tipo, 
				Date dataRecolhimento) {
		
		Date dataOperacao = distribuidorService.obter().getDataOperacao();

		PeriodoLancamentoParcial pParcial = new PeriodoLancamentoParcial();
		
		pParcial.setNumeroPeriodo(numeroPeriodo);
		pParcial.setLancamentoParcial(lancamentoParcial);
		pParcial.setDataCriacao(dataOperacao);
		pParcial.setTipo(tipo);
		pParcial.setStatus((dataRecolhimento.compareTo(dataOperacao) < 0 
				? StatusLancamentoParcial.RECOLHIDO
				: StatusLancamentoParcial.PROJETADO));

		return pParcial;
	}

	private Lancamento tratarLancamento(
			ProdutoEdicao produtoEdicao, 
			PeriodoLancamentoParcial periodo,
			LancamentoHelper helper,
			EMS0136Input input) {
		
		Date dataLancamento = input.getDataLancamento();
		Date dataRecolhimento = input.getDataRecolhimento();
		
		if (helper.getLancamentosManter() != null && !helper.getLancamentosManter().isEmpty()) {
			
			Lancamento first = helper.getLancamentosManter().first();
			
			first.setPeriodoLancamentoParcial(periodo);
			
			return first;
		}
		
		if (periodo.getPrimeiroLancamento() != null) {
			
			return periodo.getPrimeiroLancamento();
		}

		Lancamento novoLancamento = this.criarNovoLancamento(dataRecolhimento, dataLancamento, produtoEdicao);
		
		novoLancamento.setPeriodoLancamentoParcial(periodo);

		return novoLancamento;
	}
	
	private Lancamento criarNovoLancamento(Date dataRecolhimento,Date dataLancamento,ProdutoEdicao produtoEdicao){

		Lancamento lancamento = new Lancamento();

		lancamento.setDataCriacao(new Date());
		lancamento.setDataLancamentoPrevista(dataLancamento);
		lancamento.setDataLancamentoDistribuidor(dataLancamento);
		lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);
		lancamento.setDataRecolhimentoPrevista(dataRecolhimento);
		
		try {
			//lancamento.setDataLancamentoDistribuidor(getDiaMatrizAberta(input.getDataLancamento(),dataRecolhimento,message,codigoProduto,edicao));
			lancamento.setDataLancamentoDistribuidor(lancamentoService.obterDataLancamentoValido(dataLancamento, produtoEdicao.getProduto().getFornecedor().getId()));
		} catch (Exception e) {
		}
		
		lancamento.setProdutoEdicao(produtoEdicao);
		lancamento.setDataStatus(new Date());
		lancamento.setReparte(BigInteger.ZERO);
		lancamento.setRepartePromocional(BigInteger.ZERO);
		lancamento.setSequenciaMatriz(null);
		lancamento.setStatus(StatusLancamento.CONFIRMADO);
		lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
		lancamento.setNumeroLancamento(1);

		return lancamento;
	}

	private void merge(
			LancamentoParcial lancamentoParcial, 
			PeriodoLancamentoParcial periodo, 
			Lancamento lancamento, 
			ProdutoEdicao produtoEdicao) {
		
		produtoEdicao = (ProdutoEdicao) this.getSession().merge(produtoEdicao);
		
		lancamentoParcial = (LancamentoParcial) this.getSession().merge(lancamentoParcial);
		
		periodo.setLancamentoParcial(lancamentoParcial);
		
		periodo = (PeriodoLancamentoParcial) this.getSession().merge(periodo);
		
		lancamento.setPeriodoLancamentoParcial(periodo);
		
		lancamento.setProdutoEdicao(produtoEdicao);
		
		this.getSession().merge(lancamento);		
	}
	
	private void remove(Set<Lancamento> lancamentos) {
		
		for (Lancamento lancamento : lancamentos) {
			this.getSession().delete(lancamento);
			if (lancamento.getPeriodoLancamentoParcial() != null) {
				this.getSession().delete(lancamento.getPeriodoLancamentoParcial());
			}
		}
	}

	@Override
	public void posProcess(Object tempVar) { }

	final class LancamentoHelper {
		
		private SortedSet<Lancamento> lancamentosRemover = null;
		private SortedSet<Lancamento> lancamentosManter = null;
		
		private final Comparator<Lancamento> dataLancamentoComparator;

		public LancamentoHelper() {
			
			this.dataLancamentoComparator = new Comparator<Lancamento>() {
				@Override
				public int compare(Lancamento l1, Lancamento l2) {
					
					if (l1 == null || l2 == null) {
						return -1;
					}
					
					return l1.getDataLancamentoDistribuidor().compareTo(l2.getDataLancamentoDistribuidor());
				}
			};

			this.lancamentosRemover = new TreeSet<Lancamento>(this.dataLancamentoComparator);				
			this.lancamentosManter = new TreeSet<Lancamento>(this.dataLancamentoComparator);				
		}

		public SortedSet<Lancamento> getLancamentosRemover() {
			return lancamentosRemover;
		}
		public void setLancamentosRemover(SortedSet<Lancamento> lancamentosRemover) {
			this.lancamentosRemover = lancamentosRemover;
		}
		public SortedSet<Lancamento> getLancamentosManter() {
			return lancamentosManter;
		}
		public void setLancamentosExpedidos(SortedSet<Lancamento> lancamentosManter) {
			this.lancamentosManter = lancamentosManter;
		}
		
		public void addLancamentoManter(Lancamento lancamento) {
			this.lancamentosManter.add(lancamento);
		}
		
		public void addLancamentosRemover(Lancamento lancamento) {
			this.lancamentosRemover.add(lancamento);
		}		
	}
	
}