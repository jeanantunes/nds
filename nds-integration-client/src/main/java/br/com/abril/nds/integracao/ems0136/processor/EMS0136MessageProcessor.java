package br.com.abril.nds.integracao.ems0136.processor;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0136Input;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
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
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS0136MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private DistribuidorService distribuidorService;
	
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processMessage(Message message) {
		
		EMS0136Input input = (EMS0136Input) message.getBody();
		
		// Validar código do distribuidor:
		Distribuidor distribuidor = this.distribuidorService.obter();
		if(!distribuidor.getCodigoDistribuidorDinap().equals(
				input.getCodigoDistribuidor())){			
			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.RELACIONAMENTO, 
					"Código do distribuidor do arquivo não é o mesmo do Sistema.");
			return;
		}
		
		// Validar Produto/Edicao
		final String codigoProduto = input.getCodigoProduto();
		final Long numeroEdicao = input.getEdicaoCapa();
		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoProduto,
				numeroEdicao);
		if (produtoEdicao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Impossivel realizar Insert/update - Nenhum resultado encontrado para Produto: "
							+ codigoProduto + " e Edicao: " + numeroEdicao
							+ " na tabela produto_edicao");
			return;
		}
		
		LancamentoParcial lancamentoParcial = this.obterLancalmentoParcial(
				input, produtoEdicao);

		this.gerarPeriodoLancamentoParcial(input, lancamentoParcial);
	}
	
	/**
	 * Obtém o Produto Edição cadastrado previamente.
	 * 
	 * @param codigoProduto Código da Publicação.
	 * @param numeroEdicao Número da Edição.
	 * 
	 * @return
	 */
	private ProdutoEdicao obterProdutoEdicao(String codigoProduto,
			Long numeroEdicao) {

		try {

			Criteria criteria = this.getSession().createCriteria(
					ProdutoEdicao.class, "produtoEdicao");

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
	private LancamentoParcial obterLancalmentoParcial(EMS0136Input input,
			ProdutoEdicao produtoEdicao) {
		
		/* 
		 * Obtém o Lançamento Parcial já cadastrado.
		 * Caso não exista, irá criar um novo Lançamento Parcial.
		 */
		Criteria criteria = getSession().createCriteria(LancamentoParcial.class);
		criteria.add(Restrictions.eq("produtoEdicao", produtoEdicao));
		LancamentoParcial parcial = (LancamentoParcial) criteria.uniqueResult();
		if (parcial == null) {
			parcial = new LancamentoParcial();
			parcial.setProdutoEdicao(produtoEdicao);
		}
		
		/*
		 * Insere/Atualiza o restante dos dados do Lançamento Parcial vindo
		 * da Interface. 
		 */
		StatusLancamentoParcial status = this.obterStatusLancamentoParcial(
				input);	
		parcial.setStatus(status);
		
		/*
		 * Alterar a Data de Lançamento Inicial se:
		 * - Data de Lançamento for null/vazia;
		 * - Data vinda da Interface é MENOR que a do LançamentoParcial;
		 */
		if (parcial.getLancamentoInicial() == null 
				|| input.getDataLancamento().before(
						parcial.getLancamentoInicial())) {
			parcial.setLancamentoInicial(input.getDataLancamento());
		}
		
		/*
		 * Alterar a Data de Recolhimento Final se:
		 * - Data de Recolhimento for null/vazia;
		 * - Data vinda da Interface é MAIOR que a do LançamentoParcial;
		 */
		if (parcial.getRecolhimentoFinal() == null
				|| input.getDataRecolhimento().after(
						parcial.getRecolhimentoFinal())) {
			parcial.setRecolhimentoFinal(input.getDataRecolhimento());
		}
		
		if (parcial.getId() == null) {
			this.getSession().persist(parcial);
		} else {
			this.getSession().update(parcial);
		}
		
		return parcial;
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
	 * Gerar novo Período Lançamento Parcial.
	 * 
	 * @param input
	 * @param lancamentoParcial
	 * 
	 * @return
	 */
	private PeriodoLancamentoParcial gerarPeriodoLancamentoParcial(
			EMS0136Input input, LancamentoParcial lancamentoParcial) {
		
		Date dataOperacao = distribuidorService.obter().getDataOperacao();

		// Resgata todos os itens recebimentos fisicos para armazenar no novo lancamento
		List<ItemRecebimentoFisico> itens = obtemItensRecebimentosFisicos(
				lancamentoParcial, dataOperacao);
		
		/*getSession().flush();
		getSession().clear();
		Set<ItemRecebimentoFisico> itens = new HashSet<ItemRecebimentoFisico>();
		for (PeriodoLancamentoParcial periodo : lancamentoParcial.getPeriodos()) {
			if (periodo.getLancamento() != null) {
				for (ItemRecebimentoFisico item : periodo.getLancamento().getRecebimentos()) {
					itens.add(item);
				}
			}
		}*/

		/* 
		 * Exclui todos os Períodos que não foram gerados hoje (== DataOperacao)
		 */
		StringBuilder hql = new StringBuilder();
		hql.append("DELETE FROM PeriodoLancamentoParcial p ");
		hql.append(" WHERE p.lancamentoParcial = :lancamentoParcial ");
		hql.append("   AND (p.dataCriacao <> :dataOperacao ");
		hql.append("    OR p.dataCriacao IS NULL)");
		
		Query query = getSession().createQuery(hql.toString());
		query.setDate("dataOperacao", dataOperacao);
		query.setParameter("lancamentoParcial", lancamentoParcial);
		query.executeUpdate();
		
		// Executa as exclusões e limpa a sessão.
		getSession().flush();
		getSession().clear();

		
		Integer numeroPeriodo = input.getNumeroPeriodo();
		Criteria criteria = getSession().createCriteria(
				PeriodoLancamentoParcial.class);
		criteria.add(Restrictions.eq("lancamentoParcial", lancamentoParcial));
		criteria.add(Restrictions.eq("numeroPeriodo", numeroPeriodo));
		PeriodoLancamentoParcial pParcial = (PeriodoLancamentoParcial) criteria.uniqueResult();
		
		if (pParcial == null) {
			pParcial = new PeriodoLancamentoParcial();
			pParcial.setNumeroPeriodo(numeroPeriodo);
			pParcial.setLancamentoParcial(lancamentoParcial);
		}

		Lancamento lancamento = this.obterLancamento(input, 
				lancamentoParcial.getProdutoEdicao());
		lancamento.setRecebimentos(new HashSet(itens));
		pParcial.setLancamento(lancamento);
		pParcial.setDataCriacao(dataOperacao);
		pParcial.setTipo(this.obterTipoLancamentoParcial(input));
		pParcial.setStatus((input.getDataRecolhimento().compareTo(new Date()) < 0 
				? StatusLancamentoParcial.RECOLHIDO
				: StatusLancamentoParcial.PROJETADO));

		input.getDataRecolhimento();
		if (pParcial.getId() == null) {
			this.getSession().persist(pParcial);
		} else {
			this.getSession().update(pParcial);
		}
		
		return pParcial;
	}

	private List<ItemRecebimentoFisico> obtemItensRecebimentosFisicos(
			LancamentoParcial lancamentoParcial, Date dataOperacao) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT l.recebimentos ");
		hql.append("	FROM  PeriodoLancamentoParcial p ");
		hql.append("	JOIN  p.lancamento l ");
		hql.append(" WHERE p.lancamentoParcial = :lancamentoParcial ");
		hql.append("   AND (p.dataCriacao <> :dataOperacao ");
		hql.append("    OR p.dataCriacao IS NULL)");
		
		Query query = getSession().createQuery(hql.toString());
		query.setDate("dataOperacao", dataOperacao);
		query.setParameter("lancamentoParcial", lancamentoParcial);
		return (List<ItemRecebimentoFisico>) query.list();
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
	private Lancamento obterLancamento(EMS0136Input input,
			ProdutoEdicao produtoEdicao) {
		
		/*
		 * Verifica se existe um lançamento já criado anteriormente (via outra
		 * EMS).
		 * Em caso positivo, irá apenas alterar alguns status.
		 * Em caso negativo, irá gerar um novo lançamento.
		 */
		Criteria criteria = getSession().createCriteria(Lancamento.class);
		
		Date dtLancamento = input.getDataLancamento();
		Criterion criDataPrevista = Restrictions.eq(
				"dataLancamentoPrevista", dtLancamento);
		Criterion criDataDistribuidor = Restrictions.eq(
				"dataLancamentoDistribuidor", dtLancamento);
		criteria.add(Restrictions.or(criDataPrevista, criDataDistribuidor));
		criteria.add(Restrictions.eq("produtoEdicao", produtoEdicao));
		
		criteria.setMaxResults(1);
		Lancamento lancamento = (Lancamento) criteria.uniqueResult();
		if (lancamento == null) {
			lancamento = new Lancamento();
			
			Date dtAgora = new Date();
			Date dtRecolhimento = input.getDataRecolhimento();
			
			lancamento.setDataCriacao(dtAgora);
			lancamento.setDataLancamentoPrevista(dtLancamento);
			lancamento.setDataLancamentoDistribuidor(dtLancamento);
			lancamento.setDataRecolhimentoDistribuidor(dtRecolhimento);
			lancamento.setDataRecolhimentoPrevista(dtRecolhimento);
			lancamento.setProdutoEdicao(produtoEdicao);
			lancamento.setDataStatus(dtAgora);
			lancamento.setReparte(BigInteger.ZERO);
			lancamento.setRepartePromocional(BigInteger.ZERO);
			lancamento.setSequenciaMatriz(null);
			lancamento.setStatus(StatusLancamento.CONFIRMADO);
		}
		
		/*
		 * Insere/Atualiza o restante dos dados do Lançamento vindo da 
		 * Interface. 
		 */
		lancamento.setTipoLancamento(TipoLancamento.PARCIAL);
		if (lancamento.getId() == null) {
			this.getSession().persist(lancamento);
		} else {
			this.getSession().update(lancamento);
		}
		
		return lancamento;
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
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub

	}

}
