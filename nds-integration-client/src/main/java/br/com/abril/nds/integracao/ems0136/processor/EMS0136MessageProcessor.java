package br.com.abril.nds.integracao.ems0136.processor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0136Input;
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
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;

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

	private static final int PEB_MINIMA = 10;
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {}

	@Override
	public void processMessage(Message message) {
		
		EMS0136Input input = (EMS0136Input) message.getBody();
		
		if(!this.validarCodigoDistribuidor(message, input)){
			return;
		}
			
		ProdutoEdicao produtoEdicao = this.validarProdutoEdicao(message,input);
		
		if(produtoEdicao == null){
			return;
		}
		
		Lancamento lancamento = this.obterLancamento(input, produtoEdicao);
		
		if(!this.validarStatusLancamento(lancamento,message,input)){
	    	return;
	    }
		
		if (!isRecolhimentoValido(input, lancamento)) {
			return;
		}
		
		this.atualizarProdutoEdicaoParcial(produtoEdicao);

		lancamento = this.processarLancamento(input, produtoEdicao, lancamento,message);

		
		LancamentoParcial lancamentoParcial = this.obterLancamentoParciall(input,produtoEdicao,message);
		PeriodoLancamentoParcial periodo = this.obterPeriodoLancamentoParcial(input,lancamentoParcial);
		
		this.associarLancamentoAoPeriodo(periodo,lancamento);
		
		this.atualizaPeriodoLancamentoParcial(lancamento,periodo,lancamentoParcial);
		
		this.reajustarRedistribuicoes(periodo,input.getDataRecolhimento(), input.getDataLancamento());
		
		this.limparPeriodosSemAssociacaoParcial(periodo);
	}
	
	private boolean isRecolhimentoValido(EMS0136Input input, Lancamento lancamento) {
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		Date dataLancamento = this.parciaisService.obterDataUtilMaisProxima(input.getDataLancamento());
		Date dataRecolhimento = this.parciaisService.obterDataUtilMaisProxima(input.getDataRecolhimento());
		
		if (DateUtil.isDataInicialMaiorIgualDataFinal(dataOperacao, dataRecolhimento)) {
			
			return false;
		}
		
		if (DateUtil.obterDiferencaDias(dataLancamento, dataRecolhimento) < PEB_MINIMA) {
			
			return false;
		}

		return true;
	}

	
	private Lancamento processarLancamento(EMS0136Input input,ProdutoEdicao produtoEdicao, Lancamento lancamento,Message message) {
		
		Date dataLancamento = this.parciaisService.obterDataUtilMaisProxima(input.getDataLancamento());
		Date dataRecolhimento = this.parciaisService.obterDataUtilMaisProxima(input.getDataRecolhimento());

		if(lancamento == null){
			
			lancamento = this.criarNovoLancamento(dataRecolhimento, dataLancamento, produtoEdicao, message);
		}
		else{
			
			this.atualizarDatasLancamento(lancamento, dataRecolhimento, dataLancamento,message);
		}
		
		return lancamento;
	}

	private LancamentoParcial obterLancamentoParciall(EMS0136Input input,ProdutoEdicao produtoEdicao,Message message) {
		
		LancamentoParcial lancamentoParcial = this.consultarLancalmentoParcial(produtoEdicao);
		
		Date dataLancamento = this.parciaisService.obterDataUtilMaisProxima(input.getDataLancamento());
		Date dataRecolhimento = this.parciaisService.obterDataUtilMaisProxima(input.getDataRecolhimento());
		
		if(lancamentoParcial == null){
			
			lancamentoParcial = this.criarNovoLancamentoParcial(produtoEdicao, input, dataLancamento, dataRecolhimento,message);			
		}
		
		return lancamentoParcial;
	}

	private PeriodoLancamentoParcial obterPeriodoLancamentoParcial(EMS0136Input input, LancamentoParcial lancamentoParcial) {
		
		PeriodoLancamentoParcial periodo = this.obterPeriodo(lancamentoParcial,input);
		
		if(periodo == null){
			
			periodo = this.criarNovoPeriodoLancamento(lancamentoParcial,input);
		}
		else{
			periodo = this.atualizarPeriodo(periodo,input);
		}
		return periodo;
	}
	
	private PeriodoLancamentoParcial atualizarPeriodo(PeriodoLancamentoParcial periodo, EMS0136Input input) {
		
		Integer numeroPeriodo = periodo.anterior() == null ? 1 : periodo.anterior().getNumeroPeriodo() + 1;
		
		periodo.setTipo(this.obterTipoLancamentoParcial(input));
		periodo.setNumeroPeriodo(numeroPeriodo);
		periodo.setDataCriacao(new Date());
		
		return (PeriodoLancamentoParcial) this.getSession().merge(periodo);
	}

	private void atualizaPeriodoLancamentoParcial(Lancamento lancamento,PeriodoLancamentoParcial periodo, LancamentoParcial lancamentoParcial) {
		
		if(periodo.getNumeroPeriodo() == 1){
			
			lancamentoParcial.setLancamentoInicial(lancamento.getDataLancamentoDistribuidor());
		}
		
		if(TipoLancamentoParcial.FINAL.equals(periodo.getTipo())){
			
			lancamentoParcial.setRecolhimentoFinal(lancamento.getDataRecolhimentoDistribuidor());
		}
		
		this.getSession().persist(lancamentoParcial);
	}

	private void limparPeriodosSemAssociacaoParcial(PeriodoLancamentoParcial periodo) {
		
		if(TipoLancamentoParcial.FINAL.equals(periodo.getTipo())){
			
			List<PeriodoLancamentoParcial> periodos = 
					periodoLancamentoParcialRepository.obterProximosPeriodos(periodo.getNumeroPeriodo(),periodo.getLancamentoParcial().getId());
			
			if(!periodos.isEmpty()){
				
				for(PeriodoLancamentoParcial item : periodos){
					this.getSession().delete(item);
				}
			}
		}
	}
	
	private void reajustarRedistribuicoes(PeriodoLancamentoParcial periodo, Date dataRecolhimento, Date dataLancamento){
	
		List<Lancamento> redistribuicoesAnteriores = 
				periodoLancamentoParcialRepository.obterRedistribuicoesPosterioresAoLancamento(periodo.getId(), dataRecolhimento);
		
		if(!redistribuicoesAnteriores.isEmpty()){
	
			for(Lancamento item : redistribuicoesAnteriores){
				this.getSession().delete(item);
			}
		}
		
		List<Lancamento> redistribuicoesPosteriores = 
				periodoLancamentoParcialRepository.obterRedistribuicoesPosterioresAoLancamento(periodo.getId(), dataLancamento);
		
		if(!redistribuicoesPosteriores.isEmpty()){
			
			dataLancamento = this.parciaisService.obterDataUtilMaisProxima(dataLancamento);
			dataRecolhimento = this.parciaisService.obterDataUtilMaisProxima(dataRecolhimento);
			
			int numeroLancamento = 2;
			
			for(Lancamento item : redistribuicoesPosteriores){
				item.setNumeroLancamento(numeroLancamento);
				item.setDataRecolhimentoDistribuidor(dataRecolhimento);
				item.setDataRecolhimentoPrevista(dataRecolhimento);
				this.getSession().merge(item);
				numeroLancamento = numeroLancamento + 1;
			}
		}
	}
	
	private void associarLancamentoAoPeriodo(PeriodoLancamentoParcial periodo,Lancamento lancamento) {
		
		lancamento.setPeriodoLancamentoParcial(periodo);
		
		this.getSession().persist(lancamento);
	}

	private PeriodoLancamentoParcial criarNovoPeriodoLancamento(LancamentoParcial lancamentoParcial, EMS0136Input input) {
		
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		
		Integer numeroPeriodo = input.getNumeroPeriodo();
		
		PeriodoLancamentoParcial pParcial = new PeriodoLancamentoParcial();
		
		pParcial.setNumeroPeriodo(numeroPeriodo);
		pParcial.setLancamentoParcial(lancamentoParcial);
		pParcial.setDataCriacao(dataOperacao);
		pParcial.setTipo(this.obterTipoLancamentoParcial(input));
		pParcial.setStatus((input.getDataRecolhimento().compareTo(new Date()) < 0 
				? StatusLancamentoParcial.RECOLHIDO
				: StatusLancamentoParcial.PROJETADO));

		return (PeriodoLancamentoParcial) this.getSession().merge(pParcial);
	}

	private PeriodoLancamentoParcial obterPeriodo(LancamentoParcial lancamentoParcial, EMS0136Input input) {
		
		Integer numeroPeriodo = input.getNumeroPeriodo();

		PeriodoLancamentoParcial periodo = lancamentoParcial.getPeriodoPorNumero(numeroPeriodo);

		if (periodo == null) {

			return lancamentoParcial.obterPeriodoFinal();
		}
		
		return periodo;
	}

	private boolean validarStatusLancamento(Lancamento lancamento, Message message,EMS0136Input input) {
		
		if(lancamento == null){
			return true;
		}
		
		List<StatusLancamento> statusLancamentos = new ArrayList<>();
		
		statusLancamentos.add(StatusLancamento.CONFIRMADO);
		statusLancamentos.add(StatusLancamento.PLANEJADO);
		statusLancamentos.add(StatusLancamento.EM_BALANCEAMENTO);
		statusLancamentos.add(StatusLancamento.BALANCEADO);
		statusLancamentos.add(StatusLancamento.EXPEDIDO);
		statusLancamentos.add(StatusLancamento.FECHADO);
		
		if(!statusLancamentos.contains(lancamento.getStatus())){
			
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Impossivel realizar importação do Lançamento:" + lancamento.getId()  + " para o Produto:" 
					+ input.getCodigoProduto() + " e Edicao: " + input.getEdicaoCapa()
							+ " O lançamento já se encontra em recolhimento. ");
			return false;
		}

		if (StatusLancamento.FECHADO.equals(lancamento.getStatus())) {

			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"As data do Lançamento:" + lancamento.getId()  + " não serão alteradas, por estar com o status Fechado" +
					" para o Produto:" + input.getCodigoProduto() + " e Edicao: " + input.getEdicaoCapa());
		}
		
		return true; 
	}

	private ProdutoEdicao validarProdutoEdicao(Message message,EMS0136Input input) {
		
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
		
		return produtoEdicao;
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
	
	/**
	 * Atualiza o campo que identifica o produto edição como parcial. 
	 * 
	 * @param produtoEdicao
	 */
	private void atualizarProdutoEdicaoParcial(ProdutoEdicao produtoEdicao) {
		
		produtoEdicao.setParcial(true);
		
		this.getSession().persist(produtoEdicao);
	}

	/**
	 * Obtém o Produto Edição cadastrado previamente.
	 * 
	 * @param codigoProduto Código da Publicação.
	 * @param numeroEdicao Número da Edição.
	 * 
	 * @return
	 */
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
	 * Retorna um Lançamento Parcial para o ProdutoEdição informado.<br>
	 * Irá pesquisar no sistema se existe um Lançamento Parcial já cadastrado
	 * anteriormente e irá atualiza-lo com os dados vindo da Interface.<br>
	 * Caso não haja, irá gerar um novo Lançamento Parcial.
	 * 
	 * @param input
	 * @param produtoEdicao
	 * 
	 * @return
	 */
	private LancamentoParcial consultarLancalmentoParcial(ProdutoEdicao produtoEdicao) {
		
		Criteria criteria = getSession().createCriteria(LancamentoParcial.class);
		criteria.add(Restrictions.eq("produtoEdicao", produtoEdicao));
		
		LancamentoParcial parcial = (LancamentoParcial) criteria.uniqueResult();
		
		return (LancamentoParcial) parcial;
	}
	
	private LancamentoParcial criarNovoLancamentoParcial(ProdutoEdicao produtoEdicao, EMS0136Input input, Date dataLancamento, Date dataRecolhimento,Message message){

		LancamentoParcial parcial = new LancamentoParcial();
		
		parcial.setProdutoEdicao(produtoEdicao);
		
		StatusLancamentoParcial status = this.obterStatusLancamentoParcial(input);	
		
		parcial.setStatus(status);
		
		parcial.setLancamentoInicial(dataLancamento);
		
		parcial.setRecolhimentoFinal(dataRecolhimento);
		
		return (LancamentoParcial) this.getSession().merge(parcial);
	}

	/**
	 * Obtém o "Status Lançamento Parcial" verificando se a "Data de Operação"
	 * do sistema é maior que a "Data de Recolhimento" vinda do arquivo.<br>
	 * Em caso positivo o status é "RECOLHIDO", senão é "PROJETADO".
	 * 
	 * @param input
	 * @return
	 */
	private StatusLancamentoParcial obterStatusLancamentoParcial(
			EMS0136Input input) {
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

	
	/**
	 * Retorna um Lançamento para o ProdutoEdição informado.<br>
	 * Irá pesquisar no sistema se existe um Lançamento já cadastrado
	 * anteriormente e irá atualiza-lo com os dados vindo da Interface.<br>
	 * Caso não haja, irá gerar um novo Lançamento.
	 * 
	 * @param input
	 * @param produtoEdicao
	 * 
	 * @return
	 */
	private Lancamento obterLancamento(EMS0136Input input,ProdutoEdicao produtoEdicao) {
		
		Criteria criteria = getSession().createCriteria(Lancamento.class);
		
		Date dtLancamento = input.getDataLancamento();
		
		Criterion criDataPrevista = Restrictions.eq("dataLancamentoPrevista", dtLancamento);
		Criterion criDataDistribuidor = Restrictions.eq("dataLancamentoDistribuidor", dtLancamento);
		criteria.add(Restrictions.or(criDataPrevista, criDataDistribuidor));
		criteria.add(Restrictions.eq("produtoEdicao", produtoEdicao));
		
		criteria.setMaxResults(1);
		
		Lancamento lancamento = (Lancamento) criteria.uniqueResult();
		
		return lancamento;
	}
	
	private Lancamento criarNovoLancamento(Date dataRecolhimento,Date dataLancamento,ProdutoEdicao produtoEdicao,Message message){

		Lancamento lancamento = new Lancamento();

		
		try {
			//lancamento.setDataLancamentoDistribuidor(getDiaMatrizAberta(input.getDataLancamento(),dataRecolhimento,message,codigoProduto,edicao));
			dataLancamento = lancamentoService.obterDataLancamentoValido(dataLancamento, produtoEdicao.getProduto().getFornecedor().getId());
			dataRecolhimento = DateUtil.adicionarDias(dataLancamento, produtoEdicao.getPeb());
			
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Alteração das Datas de Lançamento e Recolhimento ("+dataLancamento+","+dataRecolhimento+")"
					+"Produto "+ produtoEdicao.getProduto().getCodigo() + " Edição " + produtoEdicao.getNumeroEdicao());
			
		} catch (Exception e) {
			
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Erro na Alteração das Datas de Lançamento e Recolhimento ("+dataLancamento+","+dataRecolhimento+")"
					+"Produto "+ produtoEdicao.getProduto().getCodigo() + " Edição " + produtoEdicao.getNumeroEdicao());
		}
		
		lancamento.setDataCriacao(new Date());
		lancamento.setDataLancamentoPrevista(dataLancamento);
		lancamento.setDataLancamentoDistribuidor(dataLancamento);
		lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);
		lancamento.setDataRecolhimentoPrevista(dataRecolhimento);
		

		lancamento.setProdutoEdicao(produtoEdicao);
		lancamento.setDataStatus(new Date());
		lancamento.setReparte(BigInteger.ZERO);
		lancamento.setRepartePromocional(BigInteger.ZERO);
		lancamento.setSequenciaMatriz(null);
		lancamento.setStatus(StatusLancamento.CONFIRMADO);
		lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
		lancamento.setNumeroLancamento(1);
					
		lancamento = (Lancamento) this.getSession().merge(lancamento);
		
		this.ndsiLoggerFactory.getLogger().logError(message,
				EventoExecucaoEnum.RELACIONAMENTO,
				"Lançamento inserido com sucesso. Lançamento e Recolhimento ("+dataLancamento+","+dataRecolhimento+")"
				+"Produto "+ produtoEdicao.getProduto().getCodigo() + " Edição " + produtoEdicao.getNumeroEdicao());
		
		return lancamento;
	}
	
	private Lancamento atualizarDatasLancamento(Lancamento lancamento,Date dataRecolhimento, Date dataLancamento,Message message){
		
		if ( lancamento.getStatus().equals(StatusLancamento.PLANEJADO) 
				|| lancamento.getStatus().equals(StatusLancamento.CONFIRMADO)
						|| lancamento.getStatus().equals(StatusLancamento.EM_BALANCEAMENTO)) {
			
			
			try {
				//lancamento.setDataLancamentoDistribuidor(getDiaMatrizAberta(input.getDataLancamento(),dataRecolhimento,message,codigoProduto,edicao));
				dataLancamento = lancamentoService.obterDataLancamentoValido(dataLancamento, lancamento.getProdutoEdicao().getProduto().getFornecedor().getId());
				dataRecolhimento = DateUtil.adicionarDias(dataLancamento, lancamento.getProdutoEdicao().getPeb());
				
				this.ndsiLoggerFactory.getLogger().logError(message,
						EventoExecucaoEnum.RELACIONAMENTO,
						"Alteração das Datas de Lançamento e Recolhimento ("+dataLancamento+","+dataRecolhimento+")"
						+"Produto "+ lancamento.getProdutoEdicao().getProduto().getCodigo() + " Edição " + lancamento.getProdutoEdicao().getNumeroEdicao());
				
			} catch (Exception e) {
				
				this.ndsiLoggerFactory.getLogger().logError(message,
						EventoExecucaoEnum.RELACIONAMENTO,
						"Erro na Alteração das Datas de Lançamento e Recolhimento ("+dataLancamento+","+dataRecolhimento+")"
						+"Produto "+ lancamento.getProdutoEdicao().getProduto().getCodigo() + " Edição " + lancamento.getProdutoEdicao().getNumeroEdicao());
			}
			
			
			lancamento.setDataLancamentoDistribuidor(dataLancamento);
			lancamento.setDataLancamentoPrevista(dataLancamento);
		}

		if ( lancamento.getStatus().equals(StatusLancamento.PLANEJADO) ||
			 lancamento.getStatus().equals(StatusLancamento.EM_BALANCEAMENTO) ||
			 lancamento.getStatus().equals(StatusLancamento.BALANCEADO) ||
			 lancamento.getStatus().equals(StatusLancamento.EXPEDIDO) ||
			 lancamento.getStatus().equals(StatusLancamento.CONFIRMADO)||
			 lancamento.getStatus().equals(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO)) {
			
			dataRecolhimento = DateUtil.adicionarDias(dataLancamento, lancamento.getProdutoEdicao().getPeb());
			
			lancamento.setDataRecolhimentoPrevista(dataRecolhimento);
			lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);
		}

		return (Lancamento) this.getSession().merge(lancamento);
	}

	/**
	 * Retorna o Tipo de Lançamento Parcial que esta definido no arquivo.
	 * 
	 * @param input
	 * 
	 * @return
	 */
	private TipoLancamentoParcial obterTipoLancamentoParcial(EMS0136Input input) {
		
		return "F".equalsIgnoreCase(input.getTipoRecolhimento()) 
					? TipoLancamentoParcial.FINAL 
					: TipoLancamentoParcial.PARCIAL;
	}

	@Override
	public void posProcess(Object tempVar) {}

}
