package br.com.abril.nds.integracao.ems0136.processor;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;

import com.google.common.collect.ImmutableList;

@Component
public class EMS0136MessageProcessor extends AbstractRepository implements MessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0136MessageProcessor.class);
	
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
	private RecolhimentoService recolhimentoService;
	
	@Autowired
	private DistribuicaoFornecedorService distribuicaoFornecedorService;

	@Autowired
	private SessionFactory sessionFactoryIcd;
	
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

	
	protected Session getSessionIcd() {

		Session session = null;
		try {
			session = sessionFactoryIcd.getCurrentSession();
		} catch(Exception e) {
            LOGGER.error("Erro ao obter sessão do Hibernate.Abril sessao", e);
		}

		if(session == null) {
			session = sessionFactoryIcd.openSession();
		}

		return session;

	}
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {}

	@Override
	public void processMessage(Message message) {
		
		EMS0136Input input = (EMS0136Input) message.getBody();
		
		if(!this.validarCodigoDistribuidor(message, input)) {
			
			return;
		}

		ProdutoEdicao produtoEdicao = this.obterProdutoEdicaoValidado(message, input);
		
		if(produtoEdicao == null) {
			return;
		}

		this.tratarDatasInput(input, produtoEdicao, message);

		LancamentoHelper helper = this.tratarLancamentosExistentes(produtoEdicao, input);
		
		if (helper.getLancamentosManter().isEmpty() && !this.validarDatasLancamento(input.getDataLancamento(), input.getDataRecolhimento(),message)) {
			this.ndsiLoggerFactory.getLogger().logWarning(
					message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Lancamento/Periodo nao alterado.Foi mantido o lancamento original sem alteracao pois nao tem lancamento a manter e peb menor que minimo ou data de recolhimento acima da data de operacao ");
			return;
		}

		LancamentoParcial lancamentoParcial = this.tratarLancamentoParcial(input, produtoEdicao);
		
		PeriodoLancamentoParcial periodo = this.tratarPeriodo(lancamentoParcial, input);
		
		Lancamento lancamento = this.tratarLancamento(produtoEdicao, periodo, helper, input);
		
		this.remove(helper.getLancamentosRemover());

		this.merge(lancamentoParcial, periodo, lancamento, produtoEdicao);
	}
	
	private boolean  validarCodigoDistribuidor(Message message, EMS0136Input input) {
		
		if(!this.distribuidorService.isDistribuidor(new Integer(input.getCodigoDistribuidor()))) {
			
			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.RELACIONAMENTO, 
					"Código do distribuidor do arquivo não é o mesmo do Sistema."+input.getCodigoDistribuidor());
			return false;
		}
		
		return true;
	}

	private ProdutoEdicao obterProdutoEdicaoValidado(Message message, EMS0136Input input) {
		
		String codigoProduto = input.getCodigoProduto();
		Long numeroEdicao = input.getEdicaoCapa();
		
		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoProduto, numeroEdicao);
	
		if (produtoEdicao == null) {
			
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Produto "+ codigoProduto + " Edição " + numeroEdicao +" não encontrado.");
			
			Calendar c = Calendar.getInstance();
			c.setTime(input.getDataHoraExtracao());
			c.add(Calendar.DAY_OF_MONTH, 40);
			if(c.getTime().compareTo(new Date()) < 0) {
				
				input.setErro(null);
			} else {
				
				input.setErro("Produto "+ codigoProduto + " Edição " + numeroEdicao +" não encontrado.");
			}
				
			return null;
		}
		
		input.setErro(null);
		produtoEdicao.setParcial(true);

		return produtoEdicao;
	}
	
	private ProdutoEdicao obterProdutoEdicao(String codigoProduto, Long numeroEdicao) {
		
		try {

			Criteria criteria = this.getSessionIcd().createCriteria(ProdutoEdicao.class, "produtoEdicao");

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

		if(((lp != null && lp.getPeriodoFinal() == null) || (lp != null && input.getNumeroPeriodo() > lp.getPeriodoFinal().getNumeroPeriodo()))) {

			return helper;
		}

		Set<Lancamento> lancamentosDaEdicao = produtoEdicao.getLancamentos();

		for (Lancamento lancamentoAtual : lancamentosDaEdicao) {

			// manter lancamentos EXPEDIDOS ou que CONTENHA ESTUDO ( senao o estudo e' removido )
			if (LANCAMENTO_EXPEDIDO.contains(lancamentoAtual.getStatus()) || lancamentoAtual.getEstudo() != null) {
				
				int numeroLancamento = 1;
				
				if (lancamentoAtual.getPeriodoLancamentoParcial() != null) {
					numeroLancamento = lancamentoAtual.getPeriodoLancamentoParcial().getPrimeiroLancamento().getNumeroLancamento(); 
				}

				lancamentoAtual.setNumeroLancamento(numeroLancamento);
				
				if ( !"R".equalsIgnoreCase(input.getTipoRecolhimento())) { // nao recolhido. mudar
					LOGGER.warn("LANCAMENTO NAO 'E TIPO R. NAO ALTERAR DATAS");
					lancamentoAtual.setDataRecolhimentoPrevista(input.getDataRecolhimento());
					lancamentoAtual.setDataRecolhimentoDistribuidor(input.getDataRecolhimento());
				}
				helper.addLancamentoManter(lancamentoAtual);

			} else if (LANCAMENTO_ABERTO.contains(lancamentoAtual.getStatus())) {
				
				helper.addLancamentosRemover(lancamentoAtual);
			} else {
				
				helper.addLancamentoManter(lancamentoAtual);
			}
		}
		
		if (lp != null) {

			for (Lancamento lancamentoRemover : helper.getLancamentosRemover()) {
				
				lp.getPeriodos().remove(lancamentoRemover.getPeriodoLancamentoParcial());
			}
			
			lancamentosDaEdicao.removeAll(helper.getLancamentosRemover());
		}
		
		
		
	

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
	private LancamentoParcial tratarLancamentoParcial(EMS0136Input input,ProdutoEdicao produtoEdicao) {
		
		LancamentoParcial lancamentoParcial = produtoEdicao.getLancamentoParcial(); //this.consultarLancalmentoParcial(produtoEdicao);
		
		if(lancamentoParcial == null) {
			
			lancamentoParcial = this.criarNovoLancamentoParcial(produtoEdicao, input, input.getDataLancamento(), input.getDataRecolhimento());			
		}
		
		lancamentoParcial.setRecolhimentoFinal(input.getDataRecolhimento());
		
		return lancamentoParcial;
	}

	private LancamentoParcial criarNovoLancamentoParcial(ProdutoEdicao produtoEdicao, EMS0136Input input, Date dataLancamento, Date dataRecolhimento) {

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

	private void tratarDatasInput(EMS0136Input input, ProdutoEdicao produtoEdicao, Message message) {

		if ( "R".equalsIgnoreCase(input.getTipoRecolhimento())) { // ja recolhidos nao mudar datas.
			LOGGER.warn("LANCAMENTO TIPO R. NAO ALTERAR DATAS");
			return;
		    }
		
		Date dataOriginal = input.getDataLancamento();
		Date dataSugerida = lancamentoService.obterDataLancamentoValido(dataOriginal, produtoEdicao.getProduto().getFornecedor().getId());
		
		Date dataRecolhimentoOriginal = input.getDataRecolhimento();
		Date dataRecolhimentoSugerida = recolhimentoService.obterDataRecolhimentoValido(this.getProximaDataUtil(input.getDataRecolhimento(), produtoEdicao.getProduto().getFornecedor().getId(), OperacaoDistribuidor.RECOLHIMENTO),produtoEdicao.getProduto().getFornecedor().getId());

		
		if(dataOriginal.compareTo(dataSugerida) != 0) {
			
			 this.ndsiLoggerFactory.getLogger().logWarning(message,
			 		 EventoExecucaoEnum.INF_DADO_ALTERADO,
					 "Recalculando data de Lancamento (Arquivo)"
							+" de "+ DateUtil.formatarDataPTBR(dataOriginal)
							+" para "+ DateUtil.formatarDataPTBR(dataSugerida)
							+" Produto "+ produtoEdicao.getProduto().getCodigo()
							+" Edição "+ produtoEdicao.getNumeroEdicao());
			 
			 input.setDataLancamento(dataSugerida);
			 
		}
		
		if(dataRecolhimentoOriginal.compareTo(dataRecolhimentoSugerida) != 0) {
			
			 this.ndsiLoggerFactory.getLogger().logWarning(message,
			 		 EventoExecucaoEnum.INF_DADO_ALTERADO,
					 "Recalculando data de Recolhimento (Arquivo)"
							+" de "+ DateUtil.formatarDataPTBR(dataRecolhimentoOriginal)
							+" para "+ DateUtil.formatarDataPTBR(dataRecolhimentoSugerida)
							+" Produto "+ produtoEdicao.getProduto().getCodigo()
							+" Edição "+ produtoEdicao.getNumeroEdicao());
			 
			 input.setDataRecolhimento(dataRecolhimentoSugerida);
			 
		}
		
		
		
	}
	
	private boolean validarDatasLancamento(Date dataLancamento, Date dataRecolhimento,Message message) {
		
		if (DateUtil.obterDiferencaDias(dataLancamento, dataRecolhimento) < PEB_MINIMA) {
			this.ndsiLoggerFactory.getLogger().logWarning(
					message, EventoExecucaoEnum.INF_DADO_ALTERADO, "PEB CALCULADA ("+DateUtil.obterDiferencaDias(dataLancamento, dataRecolhimento)+") MENOR QUE PEB_MINIMA ("+PEB_MINIMA+")! ");
			return false;

		} else if (DateUtil.isDataInicialMaiorDataFinal(this.distribuidorService.obterDataOperacaoDistribuidor(), dataRecolhimento)) {
			this.ndsiLoggerFactory.getLogger().logWarning(
					message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Data de recolhimento ("+dataRecolhimento+") menor que data de operacao ("+this.distribuidorService.obterDataOperacaoDistribuidor()+")! ");
			
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
		
		return this.getProximaDataUtil(DateUtil.adicionarDias(data, 1), idFornecedor, operacaoDistribuidor);

	}

	private PeriodoLancamentoParcial tratarPeriodo(LancamentoParcial parcial, EMS0136Input input) {
		
		Date dataRecolhimento = input.getDataRecolhimento();
		
		if (parcial.getPeriodos() == null || parcial.getPeriodos().isEmpty()) {

			return this.criarNovoPeriodoLancamento(parcial, 1, TipoLancamentoParcial.FINAL, dataRecolhimento);
		}

		PeriodoLancamentoParcial periodo = parcial.getPeriodoPorNumero(input.getNumeroPeriodo()); 

		if (periodo == null) {
			
			periodo = this.criarNovoPeriodoLancamento(parcial, input.getNumeroPeriodo()
					, input.getTipoRecolhimento().equals("P")||input.getTipoRecolhimento().equals("R") ? TipoLancamentoParcial.PARCIAL : TipoLancamentoParcial.FINAL, dataRecolhimento);
			
			parcial.getPeriodos().add(periodo);
			return periodo;
		}
		
		PeriodoLancamentoParcial ultimoPeriodo = parcial.getPeriodoFinal();

		if(ultimoPeriodo == null) {
			
			return periodo;
		}
		
		if (ultimoPeriodo != null && (ultimoPeriodo.equals(periodo) || ultimoPeriodo.after(periodo))) {
			ultimoPeriodo.setTipo(input.getTipoRecolhimento().equals("P")|| input.getTipoRecolhimento().equals("R") ? TipoLancamentoParcial.PARCIAL : TipoLancamentoParcial.FINAL);
			return periodo;
		} 
		
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

	private Lancamento tratarLancamento(ProdutoEdicao produtoEdicao, PeriodoLancamentoParcial periodo, LancamentoHelper helper, EMS0136Input input) {
		
		Date dataLancamento = input.getDataLancamento();
		Date dataRecolhimento = input.getDataRecolhimento();
		
		if (helper.getLancamentosManter() != null && !helper.getLancamentosManter().isEmpty()) {
			
			Lancamento first = helper.getLancamentosManter().first();
			
			first.setPeriodoLancamentoParcial(periodo);
			
			return first;
		}
		
		if (periodo != null && periodo.getPrimeiroLancamento() != null) {
			
			return periodo.getPrimeiroLancamento();
		}

		Lancamento novoLancamento = this.criarNovoLancamento(dataRecolhimento, dataLancamento, produtoEdicao, periodo);
		
		return novoLancamento;
	}
	
	private Lancamento criarNovoLancamento(Date dataRecolhimento, Date dataLancamento, ProdutoEdicao produtoEdicao, PeriodoLancamentoParcial periodo) {

		Lancamento lancamento = new Lancamento();

		lancamento.setPeriodoLancamentoParcial(periodo);
		lancamento.setDataCriacao(new Date());
		lancamento.setDataLancamentoPrevista(dataLancamento);
		lancamento.setDataLancamentoDistribuidor(dataLancamento);
		lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);
		lancamento.setDataRecolhimentoPrevista(dataRecolhimento);
		
		try {
			
			if(periodo != null && periodo.getNumeroPeriodo() > 1) {
				Calendar c = Calendar.getInstance();
				c.setTime(dataLancamento);
				int fatorRelParc = distribuidorService.obter().getFatorRelancamentoParcial();
				c.add(Calendar.DAY_OF_MONTH, (fatorRelParc > 2 ? fatorRelParc - 2 : 0)); //2 é o Fator de Relancamento Parcial do PRODIN
				dataLancamento = c.getTime();
			}
			
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
		
		produtoEdicao = (ProdutoEdicao) this.getSessionIcd().merge(produtoEdicao);
		
		lancamentoParcial = (LancamentoParcial) this.getSessionIcd().merge(lancamentoParcial);
		
		periodo.setLancamentoParcial(lancamentoParcial);
		
		periodo = (PeriodoLancamentoParcial) this.getSessionIcd().merge(periodo);
		
		lancamento.setPeriodoLancamentoParcial(periodo);
		
		lancamento.setProdutoEdicao(produtoEdicao);
		
		this.getSessionIcd().merge(lancamento);		
	}
	
	private void remove(Set<Lancamento> lancamentos) {
		
		
		for (Lancamento lancamento : lancamentos) {
		try {
		  LOGGER.warn("REMOVENDO LANCAMENTO: "+lancamento.getNumeroLancamento()+" "+lancamento.getDataLancamentoDistribuidor()+" "+lancamento.getDataRecolhimentoDistribuidor()+" reparte="+lancamento.getReparte()+
				  " plp="+lancamento.getPeriodoLancamentoParcial()+"  produto="+lancamento.getProdutoEdicao().getProduto().getCodigo()+
				  " edicao="+lancamento.getProdutoEdicao().getNumeroEdicao());
		} catch ( Exception e){
			LOGGER.error("ERRO LANCAMENTO "+lancamento.getId());
		}
			
			this.getSessionIcd().delete(lancamento);
			if (lancamento.getPeriodoLancamentoParcial() != null) {
				this.getSessionIcd().delete(lancamento.getPeriodoLancamentoParcial());
			}
		}
	}

	@Override
	public void posProcess(Object tempVar) {
		
		atualizarLancamentosSemPeriodoFinal();
		atualizarLancamentosComMaisdeUmPeriodoFinal();
	}
	
	private void atualizarLancamentosSemPeriodoFinal() {
		
		StringBuilder sql = new StringBuilder();
		try {                                                                                                    
			sql.append( " update periodo_lancamento_parcial plp                                                        ")
				.append(" inner join lancamento_parcial lp on lp.ID = plp.LANCAMENTO_PARCIAL_ID                        ")
				.append(" inner join lancamento l on plp.id = l.PERIODO_LANCAMENTO_PARCIAL_ID                          ")
				.append(" inner join produto_edicao pe on pe.id = l.PRODUTO_EDICAO_ID                                  ")
				.append(" inner join produto p on p.id = pe.PRODUTO_ID                                                 ")
				.append(" inner join (                                                                                 ")
				.append(" 	select max(numero_periodo) numPeriodoFinal, l.PRODUTO_EDICAO_ID                            ")
				.append(" 	from lancamento l                                                                          ")
				.append(" 	inner join produto_edicao pe on pe.id = l.PRODUTO_EDICAO_ID                                ")
				.append(" 	inner join produto p on p.id = pe.PRODUTO_ID                                               ")
				.append(" 	inner join periodo_lancamento_parcial plp on plp.id = l.PERIODO_LANCAMENTO_PARCIAL_ID      ")
				.append(" 	inner join lancamento_parcial lp on lp.ID = plp.LANCAMENTO_PARCIAL_ID                      ")
				.append(" 	where 1 = 1                                                                                ")
			//	.append(" 	and l.status not in (:statusLancamento)                                                    ")
				.append(" 	and plp.TIPO = :parcial                                                                    ")
				.append(" 	and l.produto_edicao_id not in (                                                           ")
				.append(" 		select distinct l.PRODUTO_EDICAO_ID                                                    ")
				.append("		from lancamento l                                                                      ")
				.append("		inner join periodo_lancamento_parcial plp on plp.id = l.PERIODO_LANCAMENTO_PARCIAL_ID  ")
				.append("		inner join lancamento_parcial lp on lp.ID = plp.LANCAMENTO_PARCIAL_ID                  ")
				.append("		where 1 = 1                                                                            ")
			//	.append("		and l.status not in (:statusLancamento)                                                ")
				.append("		and plp.TIPO = :final                                                                  ")
				.append(" 	)                                                                                          ")
				.append(" 	group by l.PRODUTO_EDICAO_ID                                                               ")
				.append(" ) rs1 on rs1.numPeriodoFinal = plp.numero_periodo and rs1.PRODUTO_EDICAO_ID = pe.id          ")
				.append(" set plp.tipo = :final                                                                        ");
			
			SQLQuery q = getSessionIcd().createSQLQuery(sql.toString());
		//	q.setParameterList("statusLancamento", Arrays.asList(StatusLancamento.FECHADO.name()));
			q.setParameter("parcial", "PARCIAL");
			q.setParameter("final", "FINAL");
			
			q.executeUpdate();

		} catch (Exception e) {
			LOGGER.error("ERRO AJUSTANDO PARCIAL/FINAL de periodos de lancamentos parciais com mais de um FINAL",e);
			throw new RuntimeException(e);
		}
	}
	
	// Caso tenha lancamentos_parcial com mais de um periodo com tipo FINAL, ajustar  para ter PARCIAL e um FINAL
	private void atualizarLancamentosComMaisdeUmPeriodoFinal() {
		LOGGER.warn("ATUALIZANDO LANCAMENTOPARCIAL COM MAIS DE UM PERIODO COM TIPO FINAL");
		StringBuilder sql = new StringBuilder();
		Session session = this.getSessionIcd();
		try {    
			int i=1;
			// obter todos lancamento_parcial_id  que tenha periodo com mais de um FINAL
			sql.append(" select LANCAMENTO_PARCIAL_ID ")
			   .append(" from periodo_lancamento_parcial where tipo = 'FINAL' group by 1 having count(*) > 1");
			SQLQuery query = session.createSQLQuery(sql.toString());
			List <BigInteger > lancamentoParcialIds = query.list();
			LOGGER.warn("ENCONTRADOS "+lancamentoParcialIds.size());
			for( BigInteger lancamentoParcialId:lancamentoParcialIds  ) {
				LOGGER.warn("PROCESSANDO  "+lancamentoParcialId +"  i="+i++);
			
			// atualizar o status de todos os periodos deste lancamento parcial para PARCIAL
			SQLQuery q1 = session.createSQLQuery(
			" update periodo_lancamento_parcial   set tipo = 'PARCIAL' where lancamento_parcial_id = :id");
			
			q1.setParameter("id",lancamentoParcialId);
			q1.executeUpdate();
			LOGGER.warn("ATUALIZANDO PARA PARCIAL  "+lancamentoParcialId);
			// Obter maior periodo 
			StringBuilder sqlm = new StringBuilder();
			sqlm.append(" select  max(numero_periodo) ")
			   .append(" from periodo_lancamento_parcial where  lancamento_parcial_id = :id");
			
			SQLQuery querym = session.createSQLQuery(sqlm.toString());
			querym.setParameter("id",lancamentoParcialId);
			
			
			Integer numeroPeriodo = (Integer) querym.uniqueResult();
			
			LOGGER.error("OBTENDO MAIOR PERIODO  PARA ATUALIZAR PARA FINAL  "+numeroPeriodo);
			// atualizar o status do ultimo periodo deste lancamento parcial para FINAL
			SQLQuery q2 = session.createSQLQuery(
			" update periodo_lancamento_parcial a set a.tipo = 'FINAL' "+
			" where a.lancamento_parcial_id = :id  and   a.numero_periodo = :numero_periodo ");
			
			q2.setParameter("id",lancamentoParcialId);
			q2.setParameter("numero_periodo",numeroPeriodo);
			q2.executeUpdate();
			LOGGER.warn("ATUALIZADO PERIODO FINAL PARA id "+lancamentoParcialId +  "  numero_periodo "+ numeroPeriodo);
			}
			
			
			

		} catch (Exception e) {
			LOGGER.error("ERRO AJUSTANDO PARCIAL/FINAL de periodos de lancamentos parciais sem periodo FINAL",e);
			throw new RuntimeException(e);
		}
	}

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