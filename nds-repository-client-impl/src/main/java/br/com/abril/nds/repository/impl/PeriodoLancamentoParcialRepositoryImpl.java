package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.dto.PeriodoParcialDTO;
import br.com.abril.nds.dto.RedistribuicaoParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.PeriodoLancamentoParcialRepository;

@Repository
public class PeriodoLancamentoParcialRepositoryImpl extends AbstractRepositoryModel<PeriodoLancamentoParcial, Long> 
			implements PeriodoLancamentoParcialRepository {

	public PeriodoLancamentoParcialRepositoryImpl() {
		super(PeriodoLancamentoParcial.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoParcialDTO> obterPeriodosParciais(FiltroParciaisDTO filtro) {
				
		StringBuilder templateHqlReparte = new StringBuilder();
		
		templateHqlReparte.append(" ( select COALESCE(sum( case tipoMovimentoSub.operacaoEstoque when 'ENTRADA' then (movimentoSub.qtde) else (movimentoSub.qtde * -1) end),0) ")
							.append(" 	from MovimentoEstoqueCota movimentoSub ")
							.append("	join movimentoSub.tipoMovimento tipoMovimentoSub ")
							.append(" 	join movimentoSub.lancamento lancamentoSub ")
							.append("   join lancamentoSub.periodoLancamentoParcial periodoSub ")
							.append("   join lancamentoSub.produtoEdicao produtoEdicaoSub ")
							.append(" 	where produtoEdicaoSub.id = produtoEdicao.id ")
							.append("	and lancamentoSub.dataLancamentoDistribuidor >= lancamento.dataLancamentoDistribuidor ")
							.append(" 	and	lancamentoSub.dataRecolhimentoDistribuidor <= lancamento.dataRecolhimentoDistribuidor ")
							.append(" 	and tipoMovimentoSub.grupoMovimentoEstoque in (:gruposMovimentoEstoqueLancamento) ")
							.append(" 	and lancamentoSub.tipoLancamento in ( :%s ) ")
							.append(" 	and movimentoSub.movimentoEstoqueCotaFuro is null ) as %s , " );
							
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select produtoEdicao.id as idProdutoEdicao, ")
			.append(" 		periodo.numeroPeriodo as numeroPeriodo, ")
			.append("		lancamento.dataLancamentoDistribuidor as dataLancamento, ")
			.append(" 		lancamento.dataRecolhimentoDistribuidor as dataRecolhimento, ")
			.append("		lancamento.dataLancamentoPrevista as dataLancamentoPrevista, ")
			.append(" 		lancamento.dataRecolhimentoPrevista as dataRecolhimentoPrevista, ")
			.append("		produtoEdicao.origem as origem, ")
			.append("  		lancamento.id as idLancamento, ")
			.append(" 		periodo.id as idPeriodo, ");
		
		hql.append(String.format(templateHqlReparte.toString(),"tipoLancamentos", "reparte"));
		
		hql.append(String.format(templateHqlReparte.toString(),"tipoLancamentoRedistribuicao", "suplementacao"));
		
		hql.append(" ( select COALESCE( sum( case tipoMovimentoSub.operacaoEstoque when 'ENTRADA' then (movimentoSub.qtde) else (movimentoSub.qtde * -1) end),0) ")
			.append(" 	from MovimentoEstoqueCota movimentoSub ")
			.append("	join movimentoSub.tipoMovimento tipoMovimentoSub ")
			.append(" 	join movimentoSub.lancamento lancamentoSub ")
			.append("   join lancamentoSub.periodoLancamentoParcial periodoSub ")
			.append("   join lancamentoSub.produtoEdicao produtoEdicaoSub ")
			.append(" 	where produtoEdicaoSub.id = produtoEdicao.id ")
			.append("	and lancamentoSub.dataLancamentoDistribuidor >= lancamento.dataLancamentoDistribuidor ")
			.append(" 	and	lancamentoSub.dataRecolhimentoDistribuidor <= lancamento.dataRecolhimentoDistribuidor ")
			.append(" 	and tipoMovimentoSub.grupoMovimentoEstoque in (:gruposMovimentoEstoqueEncalhe) ")
			.append(" 	and movimentoSub.movimentoEstoqueCotaFuro is null ) as encalhe , " );
		
		hql.append(" ( select COALESCE(sum(item.qtdeVendaApurada),0) ") 
			.append(" from ItemChamadaEncalheFornecedor item  ")
			.append(" join item.produtoEdicao itemProdutoEdicao ")
			.append(" where itemProdutoEdicao.id = produtoEdicao.id ")
			.append(" and item.dataRecolhimento <= lancamento.dataRecolhimentoDistribuidor ")
			.append(" and item.regimeRecolhimento in ('PARCIAL','FINAL' )) as vendaCE");
		
		hql.append(getSqlFromEWherePeriodosParciais(filtro));
	
		hql.append(" group by periodo ");
				
		hql.append(" order by periodo.numeroPeriodo ");
				
		Query query =  getSession().createQuery(hql.toString());
		
		query = buscarParametrosLancamentosParciais(filtro,query);
		
		query.setParameterList("gruposMovimentoEstoqueLancamento", 
				Arrays.asList(GrupoMovimentoEstoque.ENVIO_JORNALEIRO,
							  GrupoMovimentoEstoque.FALTA_DE,
							  GrupoMovimentoEstoque.FALTA_EM,
							  GrupoMovimentoEstoque.SOBRA_DE,
							  GrupoMovimentoEstoque.SOBRA_EM,
							  GrupoMovimentoEstoque.ESTORNO_COMPRA_ENCALHE,
							  GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR,
							  GrupoMovimentoEstoque.ESTORNO_ENVIO_REPARTE,
							  GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE,
							  GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE,
							  GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR,
							  GrupoMovimentoEstoque.COMPRA_ENCALHE,
							  GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR,
							  GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE,
							  GrupoMovimentoEstoque.VENDA_ENCALHE,
							  GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR));
				
		query.setParameterList("tipoLancamentoRedistribuicao",Arrays.asList(TipoLancamento.REDISTRIBUICAO));
				
		query.setParameterList("tipoLancamentos", Arrays.asList(TipoLancamento.REDISTRIBUICAO,TipoLancamento.LANCAMENTO));
				
		query.setParameterList("gruposMovimentoEstoqueEncalhe", Arrays.asList(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE));
		
		query.setResultTransformer(new AliasToBeanResultTransformer(PeriodoParcialDTO.class));
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}
	
	private String getSqlFromEWherePeriodosParciais(FiltroParciaisDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from PeriodoLancamentoParcial periodo ");
		hql.append(" join periodo.lancamentoParcial lancamentoParcial ");
		hql.append(" join periodo.lancamentos lancamento ");
		hql.append(" join lancamento.produtoEdicao produtoEdicao ");
		hql.append(" join produtoEdicao.produto produto ");
		hql.append(" left join lancamento.estudo estudo ");
		hql.append(" left join lancamento.movimentoEstoqueCotas mCota ");
		hql.append(" join produto.fornecedores fornecedor ");
		hql.append(" join fornecedor.juridica juridica ");
		
		hql.append(" where lancamento.status <> :statusCancelado ");
		
		if(filtro.getCodigoProduto() != null) { 
			hql.append( " and  produto.codigo =:codProduto ");
		}
		
		if(filtro.getNomeProduto() != null) { 
			hql.append(" and  lower(produto.nome)like:nomeProduto ");
		}
		
		if(filtro.getEdicaoProduto() != null){ 
			hql.append(" and  produtoEdicao.numeroEdicao=:edProduto ");
		}
		
		if(filtro.getIdFornecedor() != null) { 
			hql.append(" and  fornecedor.id=:idFornecedor ");
		}
		
		if(filtro.getDataInicialDate() != null) { 
			hql.append( " and  periodo.lancamento.dataLancamentoDistribuidor>=:dtInicial ");
		}
		
		if(filtro.getDataFinalDate() != null) { 
			hql.append( " and  periodo.lancamento.dataRecolhimentoDistribuidor<=:dtFinal ");
		}
		
		if(filtro.getStatus() != null) { 
			hql.append(" and  periodo.lancamento.status=:status ");
		}
		
		return hql.toString();
	}


	/**
	 * Retorna os parametros da consulta de dividas.
	 * @param filtro
	 * @return HashMap<String,Object>
	 */
	private Query buscarParametrosLancamentosParciais(FiltroParciaisDTO filtro, Query query){
				
		if(filtro.getCodigoProduto() != null) 
			query.setParameter("codProduto", filtro.getCodigoProduto());
		
		if(filtro.getNomeProduto() != null) 
			query.setParameter("nomeProduto", "%" + filtro.getNomeProduto().toLowerCase() + "%");
		
		if(filtro.getEdicaoProduto() != null) 
			query.setParameter("edProduto", filtro.getEdicaoProduto());
		
		if(filtro.getIdFornecedor() != null) 
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		
		if(filtro.getDataInicialDate() != null) 
			query.setParameter("dtInicial", filtro.getDataInicialDate());
		
		if(filtro.getDataFinalDate() != null) 
			query.setParameter("dtFinal", filtro.getDataFinalDate());
		
		if(filtro.getStatus() != null) 
			query.setParameter("status", filtro.getStatusEnum());
		
		query.setParameter("statusCancelado", StatusLancamento.CANCELADO);
		
		return query;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer totalObterPeriodosParciais(FiltroParciaisDTO filtro) {
		
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select distinct periodo ");
		
		hql.append(getSqlFromEWherePeriodosParciais(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		query = buscarParametrosLancamentosParciais(filtro,query);
		
		List<Object> listaTotalRegistros = query.list();
		Long totalRegistros = (long) listaTotalRegistros.size();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}
	
	public PeriodoLancamentoParcial obterPeriodoPorIdLancamento(Long idLancamento) {
		
		Criteria criteria = super.getSession().createCriteria(Lancamento.class,"lancamento");
				
		criteria.add(Restrictions.eq("lancamento.id", idLancamento));
		
		Lancamento lancamento = (Lancamento) criteria.uniqueResult();
		
		return (lancamento == null)? null : lancamento.getPeriodoLancamentoParcial();
	}

	/**
	 * Obtem detalhes das vendas do produtoEdição nas datas de Lancamento e Recolhimento
	 * @param dataLancamento
	 * @param dataRecolhimento
	 * @param idProdutoRdicao
	 * @return List<ParcialVendaDTO>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ParcialVendaDTO> obterDetalhesVenda(Date dataLancamento, Date dataRecolhimento, Long idProdutoEdicao) {
		
        StringBuilder hql = new StringBuilder();
		
		hql.append(" select cota.numeroCota as numeroCota, ");
		hql.append(" pessoa.nome as nomeCota, ");
		hql.append(" epc.qtdeRecebida as reparte, ");
		hql.append(" epc.qtdeDevolvida as encalhe, ");
	    hql.append(" case when conferencia.juramentada = true then conferencia.qtde else 0 end as vendaJuramentada ");
		
		hql.append(" from ConferenciaEncalhe conferencia ");
		hql.append("	join conferencia.movimentoEstoqueCota movimento ");
		hql.append("	join conferencia.chamadaEncalheCota chamadaEncalheCota ");
		hql.append("	join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ");
		hql.append("	join movimento.estoqueProdutoCota epc ");
		hql.append("	join movimento.cota cota ");
		hql.append("	join epc.produtoEdicao produtoEdicao ");
		hql.append("	join cota.pessoa pessoa ");
		
		hql.append("	where chamadaEncalhe.dataRecolhimento >= :dataLancamento ");
		hql.append("	and chamadaEncalhe.dataRecolhimento <= :dataRecolhimento ");
		hql.append("	and produtoEdicao.id = :idProdutoEdicao ");
				
		Query query =  getSession().createQuery(hql.toString());
		
		query.setParameter("dataLancamento",dataLancamento);
		query.setParameter("dataRecolhimento",dataRecolhimento);
		query.setParameter("idProdutoEdicao",idProdutoEdicao);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ParcialVendaDTO.class));
		
		return query.list();
	}
	
	@Override
	public Lancamento obterLancamentoAnterior(Long idProdutoEdicao, Date dataLancamento) {
					
		Criteria criteria = super.getSession().createCriteria(Lancamento.class,"lancamento");
		
		criteria.createAlias("lancamento.periodoLancamentoParcial","periodo");
		
		criteria.createAlias("lancamento.produtoEdicao","produtoEdicao");
		
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
		
		criteria.add(Restrictions.lt("lancamento.dataLancamentoDistribuidor", dataLancamento));
		
		criteria.addOrder(Order.asc("lancamento.dataLancamentoDistribuidor"));  
		
		criteria.setMaxResults(1);
		
		return (Lancamento) criteria.uniqueResult();
	
	}
	
	public PeriodoLancamentoParcial obterPrimeiroLancamentoParcial(Long idProdutoEdicao){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select periodo from PeriodoLancamentoParcial periodo  ")
			.append(" join periodo.lancamentos lancamento join lancamento.produtoEdicao produtoEdicao ")
			.append(" where lancamento.dataLancamentoDistribuidor = ")
			.append(" ( select min(l.dataLancamentoDistribuidor) from PeriodoLancamentoParcial lp join lp.lancamentos l join l.produtoEdicao e where e.id = :idProdutoEdicao and l.tipoLancamento=:tipoLancamento  ) ")
			.append(" and produtoEdicao.id =:idProdutoEdicao ")
			.append(" and lancamento.tipoLancamento=:tipoLancamento ");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("tipoLancamento", TipoLancamento.LANCAMENTO);
		
		return (PeriodoLancamentoParcial) query.uniqueResult();
	}
	
	public PeriodoLancamentoParcial obterUltimoLancamentoParcial(Long idProdutoEdicao){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select periodo from PeriodoLancamentoParcial periodo  ")
			.append(" join periodo.lancamentos lancamento join lancamento.produtoEdicao produtoEdicao ")
			.append(" where lancamento.dataLancamentoDistribuidor = ")
			.append(" ( select max(l.dataLancamentoDistribuidor) from PeriodoLancamentoParcial lp join lp.lancamentos l join l.produtoEdicao e where e.id = :idProdutoEdicao and l.tipoLancamento=:tipoLancamento  ) ")
			.append(" and produtoEdicao.id =:idProdutoEdicao ")
			.append(" and lancamento.tipoLancamento=:tipoLancamento");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("tipoLancamento", TipoLancamento.LANCAMENTO);
		
		return (PeriodoLancamentoParcial) query.uniqueResult();
	
	}
	
	public Long obterQntPeriodosAposBalanceamentoRealizado(Long idLancamentoParcial){
		   
		StringBuilder hql = new StringBuilder();

		hql.append(" select count( periodo.id ) from PeriodoLancamentoParcial periodo  ")

		.append(" join periodo.lancamentos lancamento ")

		.append(" join periodo.lancamentoParcial lancamentoParcial ")

		.append(" where lancamento.status not in (:satatusLancamento) ")

		.append(" and lancamentoParcial.id =:idLancamentoParcial ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idLancamentoParcial", idLancamentoParcial);

		query.setParameterList("satatusLancamento", Arrays.asList(StatusLancamento.CONFIRMADO,
																  StatusLancamento.PLANEJADO,
																  StatusLancamento.EM_BALANCEAMENTO,
																  StatusLancamento.BALANCEADO));

		return (Long) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RedistribuicaoParcialDTO> obterRedistribuicoesParciais(Long idPeriodo) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append(" lancamento.id as idLancamentoRedistribuicao, ");
		hql.append(" periodoLancamentoParcial.id as idPeriodo, ");
		hql.append(" periodoLancamentoParcial.numeroPeriodo as numeroPeriodo, ");
		hql.append(" lancamento.numeroLancamento as numeroLancamento, ");
		hql.append(" lancamento.dataLancamentoDistribuidor as dataLancamento, ");
		hql.append(" lancamento.dataRecolhimentoDistribuidor as dataRecolhimento ");
		
		hql.append(" from PeriodoLancamentoParcial periodoLancamentoParcial ");
		hql.append(" join periodoLancamentoParcial.lancamentos lancamento ");
		hql.append(" where periodoLancamentoParcial.id = :idPeriodo ");
		hql.append(" order by lancamento.numeroLancamento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idPeriodo", idPeriodo);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RedistribuicaoParcialDTO.class));
		
		return query.list();
	}
	
	public PeriodoLancamentoParcial obterPeriodoPorNumero(Integer numeroPeriodo, Long idLancamentoParcial){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select periodoLancamentoParcial from PeriodoLancamentoParcial periodoLancamentoParcial ");
		hql.append(" where periodoLancamentoParcial.numeroPeriodo = :numeroPeriodo ");
		hql.append(" and periodoLancamentoParcial.lancamentoParcial.id = :idLancamentoParcial ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("numeroPeriodo", numeroPeriodo);
		query.setParameter("idLancamentoParcial", idLancamentoParcial);
		query.setMaxResults(1);
		
		return (PeriodoLancamentoParcial) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> obterRedistribuicoesAnterioresAoLancamento(Long idPeriodo,Date dataRecolhimento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select lancamento  ");
		hql.append(" from PeriodoLancamentoParcial periodoLancamentoParcial ");
		hql.append(" join periodoLancamentoParcial.lancamentos lancamento ");
		hql.append(" where periodoLancamentoParcial.id = :idPeriodo ");
		hql.append(" and lancamento.dataLancamentoDistribuidor > :dataRecolhimento ");
		hql.append(" and lancamento.tipoLancamento =:tipoLancamento ");
		hql.append(" order by lancamento.numeroLancamento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idPeriodo", idPeriodo);
		query.setParameter("dataLancamento", dataRecolhimento);
		query.setParameter("tipoLancamento", TipoLancamento.REDISTRIBUICAO);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> obterRedistribuicoesPosterioresAoLancamento(Long idPeriodo,Date dataLancamento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select lancamento  ");
		hql.append(" from PeriodoLancamentoParcial periodoLancamentoParcial ");
		hql.append(" join periodoLancamentoParcial.lancamentos lancamento ");
		hql.append(" where periodoLancamentoParcial.id = :idPeriodo ");
		hql.append(" and lancamento.dataLancamentoDistribuidor >= :dataLancamento ");
		hql.append(" and lancamento.tipoLancamento =:tipoLancamento ");
		hql.append(" order by lancamento.numeroLancamento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idPeriodo", idPeriodo);
		query.setParameter("dataLancamento", dataLancamento);
		query.setParameter("tipoLancamento", TipoLancamento.REDISTRIBUICAO);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> obterRedistribuicoes(Long idPeriodo) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select lancamento  ");
		hql.append(" from PeriodoLancamentoParcial periodoLancamentoParcial ");
		hql.append(" join periodoLancamentoParcial.lancamentos lancamento ");
		hql.append(" where periodoLancamentoParcial.id = :idPeriodo ");
		hql.append(" and lancamento.tipoLancamento =:tipoLancamento ");
		hql.append(" order by lancamento.numeroLancamento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idPeriodo", idPeriodo);
		query.setParameter("tipoLancamento", TipoLancamento.REDISTRIBUICAO);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoLancamentoParcial> obterProximosPeriodos(Integer numeroPeriodo,Long idLancamentoParcial) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select periodoLancamentoParcial  ");
		hql.append(" from PeriodoLancamentoParcial periodoLancamentoParcial ");
		hql.append(" join periodoLancamentoParcial.lancamentos lancamento ");
		hql.append(" join periodoLancamentoParcial.lancamentoParcial lancamentoParcial ");
		hql.append(" where lancamentoParcial.id = :idLancamentoParcial ");
		hql.append(" and lancamento.tipoLancamento =:tipoLancamento ");
		hql.append(" and periodoLancamentoParcial.numeroPeriodo > :numeroPeriodo");
		
		hql.append(" order by periodoLancamentoParcial.numeroPeriodo asc ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idLancamentoParcial", idLancamentoParcial);
		query.setParameter("numeroPeriodo", numeroPeriodo);
		query.setParameter("tipoLancamento", TipoLancamento.LANCAMENTO);
		
		return query.list();
	}
	
	@Override
	public boolean isLancamentoConferenciaEncalheCotaPeriodoFinal(Long idProdutoEdicao, Long idCota, Date dataRecolhimento) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select count(distinct periodoLancamento.ID) ")
			.append("from ")
			.append("chamada_encalhe chamadaEncalhe ")
			.append("join ")
			.append("chamada_encalhe_cota chamadaEncalheCota ON chamadaEncalhe.ID = chamadaEncalheCota.CHAMADA_ENCALHE_ID ")
			.append("join ")
			.append("chamada_encalhe_lancamento chamadaEncalheLancamento ON chamadaEncalheLancamento.CHAMADA_ENCALHE_ID = chamadaEncalhe.ID ")
			.append("join ")
			.append("lancamento lancamento ON lancamento.ID = chamadaEncalheLancamento.LANCAMENTO_ID ")
			.append("join ")
			.append("produto_edicao produtoEdicao ON produtoEdicao.ID = chamadaEncalhe.PRODUTO_EDICAO_ID ")
			.append("join ")
			.append("periodo_lancamento_parcial periodoLancamento ON periodoLancamento.ID = lancamento.PERIODO_LANCAMENTO_PARCIAL_ID ")
			.append("where produtoEdicao.ID = :idProdutoEdicao ")
			.append("and lancamento.TIPO_LANCAMENTO = :tipoLancamento ")
			.append("and chamadaEncalhe.DATA_RECOLHIMENTO = :dataRecolhimento ")
			.append("and lancamento.DATA_REC_DISTRIB = :dataRecolhimento ")
			.append("and periodoLancamento.TIPO = :tipoLancamentoParcial ")
			.append("and chamadaEncalheCota.COTA_ID = :idCota ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("idCota", idCota);
		query.setParameter("dataRecolhimento", dataRecolhimento);
		query.setParameter("tipoLancamentoParcial", TipoLancamentoParcial.FINAL);
		query.setParameter("tipoLancamento", TipoLancamento.LANCAMENTO);
		
		BigInteger retorno = (BigInteger) query.uniqueResult() ; 
		
		return ( retorno != null && retorno.intValue() > 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PeriodoLancamentoParcial obterPeriodoAnterior(Date dataRecolhimento, Long idProdutoEdicao) {

		String sql = String.format(this.obterConsultaObtencaoPeriodo(), "<", "desc");
		
		return this.obterPeriodoProximo(sql, dataRecolhimento, idProdutoEdicao);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PeriodoLancamentoParcial obterPeriodoPosterior(Date dataRecolhimento, Long idProdutoEdicao) {

		String sql = String.format(this.obterConsultaObtencaoPeriodo(), ">", "asc");
		
		return this.obterPeriodoProximo(sql, dataRecolhimento, idProdutoEdicao);
	}
	
	private PeriodoLancamentoParcial obterPeriodoProximo(String sql, Date dataRecolhimento, Long idProdutoEdicao) {

		SQLQuery query = getSession().createSQLQuery(sql);
		
		query.setParameter("dataRecolhimento", dataRecolhimento);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.addEntity(PeriodoLancamentoParcial.class);
		
		query.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		
		query.setMaxResults(1);
		
		return (PeriodoLancamentoParcial) query.uniqueResult();
	}

	private String obterConsultaObtencaoPeriodo() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select plp.*   							");
        sql.append(" from lancamento l                          ");
	  	sql.append(" join produto_edicao pe                     ");
        sql.append(" on pe.ID=l.PRODUTO_EDICAO_ID               ");
        sql.append(" join lancamento_parcial lp                 ");
        sql.append(" on lp.PRODUTO_EDICAO_ID=pe.ID              ");
        sql.append(" join periodo_lancamento_parcial plp        ");
        sql.append(" on  plp.LANCAMENTO_PARCIAL_ID=lp.ID        ");
		sql.append(" and plp.ID=l.PERIODO_LANCAMENTO_PARCIAL_ID ");
		sql.append(" where pe.ID = :idProdutoEdicao             ");
        sql.append(" and l.DATA_REC_DISTRIB %s :dataRecolhimento");
		sql.append(" order by l.DATA_REC_DISTRIB %s             ");

		return sql.toString();
	}
}
