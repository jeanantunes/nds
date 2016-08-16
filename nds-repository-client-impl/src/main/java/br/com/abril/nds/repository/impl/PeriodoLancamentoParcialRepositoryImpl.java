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
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.dto.PeriodoParcialDTO;
import br.com.abril.nds.dto.RedistribuicaoParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoEstudoCota;
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
				
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT STRAIGHT_JOIN produtoedi3_.ID AS idProdutoEdicao, ");
		sql.append(" periodolan0_.NUMERO_PERIODO AS numeroPeriodo, ");
		sql.append(" lancamento2_.DATA_LCTO_DISTRIBUIDOR AS dataLancamento, ");
		sql.append(" lancamento2_.DATA_REC_DISTRIB AS dataRecolhimento, ");
		sql.append(" lancamento2_.DATA_LCTO_PREVISTA AS dataLancamentoPrevista, ");
		sql.append(" lancamento2_.DATA_REC_PREVISTA AS dataRecolhimentoPrevista, ");
		sql.append(" produtoedi3_.ORIGEM AS origem, ");
		sql.append(" lancamento2_.ID AS idLancamento, ");
		sql.append(" lancamento2_.STATUS AS statusLancamento, ");
		sql.append(" periodolan0_.ID AS idPeriodo, ");

		sql.append(" (SELECT coalesce(sum(CASE tipomovime11_.OPERACAO_ESTOQUE WHEN 'ENTRADA' THEN movimentoe10_.QTDE ELSE (movimentoe10_.QTDE*-1) END), 0) ");
		sql.append(" FROM MOVIMENTO_ESTOQUE_COTA movimentoe10_ ");
		sql.append(" INNER JOIN TIPO_MOVIMENTO tipomovime11_ ON movimentoe10_.TIPO_MOVIMENTO_ID=tipomovime11_.ID ");
		sql.append(" INNER JOIN LANCAMENTO lancamento12_ ON movimentoe10_.LANCAMENTO_ID=lancamento12_.ID ");
		sql.append(" INNER JOIN PERIODO_LANCAMENTO_PARCIAL periodolan13_ ON lancamento12_.PERIODO_LANCAMENTO_PARCIAL_ID=periodolan13_.ID ");
		sql.append(" INNER JOIN PRODUTO_EDICAO produtoedi14_ ON lancamento12_.PRODUTO_EDICAO_ID=produtoedi14_.ID ");
		sql.append(" LEFT OUTER JOIN MOVIMENTO_ESTOQUE_COTA movimentoe15_ ON movimentoe10_.MOVIMENTO_ESTOQUE_COTA_FURO_ID=movimentoe15_.ID ");
		sql.append(" WHERE produtoedi14_.ID=produtoedi3_.ID ");
		sql.append(" AND periodolan13_.ID=periodolan0_.ID ");
		sql.append(" AND (lancamento12_.DATA_LCTO_DISTRIBUIDOR>=lancamento2_.DATA_LCTO_DISTRIBUIDOR OR lancamento12_.DATA_REC_DISTRIB<=lancamento2_.DATA_REC_DISTRIB) ");
		sql.append(" AND (tipomovime11_.GRUPO_MOVIMENTO_ESTOQUE IN (:gruposMovimentoEstoqueLancamento)) ");
		sql.append(" AND (lancamento12_.TIPO_LANCAMENTO IN (:tipoLancamento)) ");
		sql.append(" AND (movimentoe15_.ID IS NULL)) AS reparte, ");

		sql.append(" (SELECT coalesce(sum(CASE tipomovime17_.OPERACAO_ESTOQUE WHEN 'ENTRADA' THEN movimentoe16_.QTDE ELSE (movimentoe16_.QTDE*-1) END), 0) ");
		sql.append(" FROM MOVIMENTO_ESTOQUE_COTA movimentoe16_ ");
		sql.append(" INNER JOIN TIPO_MOVIMENTO tipomovime17_ ON movimentoe16_.TIPO_MOVIMENTO_ID=tipomovime17_.ID ");
		sql.append(" INNER JOIN LANCAMENTO lancamento18_ ON movimentoe16_.LANCAMENTO_ID=lancamento18_.ID ");
		sql.append(" INNER JOIN PERIODO_LANCAMENTO_PARCIAL periodolan19_ ON lancamento18_.PERIODO_LANCAMENTO_PARCIAL_ID=periodolan19_.ID ");
		sql.append(" INNER JOIN PRODUTO_EDICAO produtoedi20_ ON lancamento18_.PRODUTO_EDICAO_ID=produtoedi20_.ID ");
		sql.append(" LEFT OUTER JOIN MOVIMENTO_ESTOQUE_COTA movimentoe21_ ON movimentoe16_.MOVIMENTO_ESTOQUE_COTA_FURO_ID=movimentoe21_.ID ");
		sql.append(" WHERE produtoedi20_.ID=produtoedi3_.ID ");
		sql.append(" AND periodolan19_.ID=periodolan0_.ID ");
		sql.append(" AND (lancamento18_.DATA_LCTO_DISTRIBUIDOR>=lancamento2_.DATA_LCTO_DISTRIBUIDOR OR lancamento18_.DATA_REC_DISTRIB<=lancamento2_.DATA_REC_DISTRIB) ");
		sql.append(" AND (tipomovime17_.GRUPO_MOVIMENTO_ESTOQUE IN (:gruposMovimentoEstoqueLancamento)) ");
		sql.append(" AND (lancamento18_.TIPO_LANCAMENTO IN (:tipoLancamentoRedistribuicao)) ");
		sql.append(" AND (movimentoe21_.ID IS NULL)) AS suplementacao, ");

		sql.append(" (SELECT COALESCE(SUM(CASE tipomovime23_.OPERACAO_ESTOQUE WHEN 'SAIDA' THEN movimentoe22_.QTDE ELSE movimentoe22_.QTDE*-1 END), 0) ");
		sql.append(" FROM MOVIMENTO_ESTOQUE_COTA movimentoe22_ ");
		sql.append(" INNER JOIN TIPO_MOVIMENTO tipomovime23_ ON movimentoe22_.TIPO_MOVIMENTO_ID=tipomovime23_.ID ");
		sql.append(" INNER JOIN conferencia_encalhe ce ON ce.MOVIMENTO_ESTOQUE_COTA_ID=movimentoe22_.ID ");
		sql.append(" INNER JOIN CHAMADA_ENCALHE_COTA cec ON ce.CHAMADA_ENCALHE_COTA_ID=cec.ID ");
		sql.append(" INNER JOIN chamada_encalhe che ON che.ID=cec.CHAMADA_ENCALHE_ID ");
		sql.append(" INNER JOIN CHAMADA_ENCALHE_LANCAMENTO cel ON cel.CHAMADA_ENCALHE_ID=che.ID ");
		sql.append(" INNER JOIN LANCAMENTO lancamento24_ ON cel.LANCAMENTO_ID=lancamento24_.ID ");
		sql.append(" INNER JOIN PRODUTO_EDICAO produtoedi26_ ON lancamento24_.PRODUTO_EDICAO_ID=produtoedi26_.ID ");
		sql.append(" INNER JOIN PERIODO_LANCAMENTO_PARCIAL periodolan25_ ON lancamento24_.PERIODO_LANCAMENTO_PARCIAL_ID=periodolan25_.ID ");
		sql.append(" LEFT OUTER JOIN MOVIMENTO_ESTOQUE_COTA movimentoe27_ ON movimentoe22_.MOVIMENTO_ESTOQUE_COTA_FURO_ID=movimentoe27_.ID ");
		sql.append(" WHERE produtoedi26_.ID=produtoedi3_.ID ");
		sql.append(" AND periodolan25_.ID=periodolan0_.ID ");
		sql.append(" AND lancamento24_.DATA_LCTO_DISTRIBUIDOR>=lancamento2_.DATA_LCTO_DISTRIBUIDOR ");
		sql.append(" AND lancamento24_.DATA_REC_DISTRIB<=lancamento2_.DATA_REC_DISTRIB ");
		sql.append(" AND (movimentoe27_.ID IS NULL) ");
		sql.append(" AND (tipomovime23_.GRUPO_MOVIMENTO_ESTOQUE IN (:gruposMovimentoEstoqueEncalhe)) ");
		sql.append(" AND (movimentoe27_.ID IS NULL)) AS  encalhe, ");


		sql.append(" (SELECT coalesce(sum(itemchamad28_.QTDE_VENDA_APURADA), 0) ");
		sql.append(" FROM ITEM_CHAMADA_ENCALHE_FORNECEDOR itemchamad28_ ");
		sql.append(" INNER JOIN PRODUTO_EDICAO produtoedi29_ ON itemchamad28_.PRODUTO_EDICAO_ID=produtoedi29_.ID ");
		sql.append(" WHERE produtoedi29_.ID=produtoedi3_.ID ");
		sql.append(" AND itemchamad28_.DATA_RECOLHIMENTO<=lancamento2_.DATA_REC_DISTRIB ");
		sql.append(" AND (itemchamad28_.REGIME_RECOLHIMENTO IN ('PARCIAL' , 'FINAL'))) AS vendaCE ");
		
		sql.append(" FROM PERIODO_LANCAMENTO_PARCIAL periodolan0_ ");
		sql.append(" INNER JOIN LANCAMENTO_PARCIAL lancamento1_ ON periodolan0_.LANCAMENTO_PARCIAL_ID=lancamento1_.ID ");
		
		// se tiver index NDX_STATUS_IDX ignorar na query, por problema de performance
		List list= getSession().createSQLQuery("show index from lancamento where Key_name = 'NDX_STATUS_IDX'").list();
		if ( list != null && !list.isEmpty())
		  sql.append(" INNER JOIN LANCAMENTO lancamento2_ ignore index ( NDX_STATUS_IDX) ON periodolan0_.ID=lancamento2_.PERIODO_LANCAMENTO_PARCIAL_ID ");
		else
		 sql.append(" INNER JOIN LANCAMENTO lancamento2_  ON periodolan0_.ID=lancamento2_.PERIODO_LANCAMENTO_PARCIAL_ID ");
	
		sql.append(" INNER JOIN PRODUTO_EDICAO produtoedi3_ ON lancamento2_.PRODUTO_EDICAO_ID=produtoedi3_.ID ");
		sql.append(" INNER JOIN PRODUTO produto4_ ON produtoedi3_.PRODUTO_ID=produto4_.ID ");
		sql.append(" INNER JOIN PRODUTO_FORNECEDOR fornecedor7_ ON produto4_.ID=fornecedor7_.PRODUTO_ID ");
		sql.append(" INNER JOIN FORNECEDOR fornecedor8_ ON fornecedor7_.fornecedores_ID=fornecedor8_.ID ");
		sql.append(" INNER JOIN PESSOA pessoajuri9_ ON fornecedor8_.JURIDICA_ID=pessoajuri9_.ID ");
		sql.append(" LEFT OUTER JOIN ESTUDO estudo5_ ON lancamento2_.ESTUDO_ID=estudo5_.ID ");
		sql.append(" LEFT OUTER JOIN MOVIMENTO_ESTOQUE_COTA movimentoe6_ ON lancamento2_.ID=movimentoe6_.LANCAMENTO_ID ");
		sql.append(" WHERE lancamento2_.STATUS <> :statusCancelado ");
		sql.append(" AND produto4_.CODIGO = :codProduto ");
		sql.append(" AND produtoedi3_.NUMERO_EDICAO = :edProduto ");

		if(filtro != null && filtro.getDataInicial() != null) {
			
			sql.append(" AND lancamento2_.DATA_LCTO_DISTRIBUIDOR >= :dtInicial ");
		}
		
		if(filtro != null && filtro.getDataFinal() != null) {
			
			sql.append(" AND lancamento2_.DATA_LCTO_DISTRIBUIDOR <= :dtFinal ");
		}
		
		sql.append(" GROUP BY periodolan0_.ID ");
		sql.append(" ORDER BY periodolan0_.NUMERO_PERIODO ");
		

		
		Query query =  getSession().createSQLQuery(sql.toString());
		
		((SQLQuery) query).addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		((SQLQuery) query).addScalar("numeroPeriodo", StandardBasicTypes.INTEGER);
		((SQLQuery) query).addScalar("dataLancamento", StandardBasicTypes.DATE);
		((SQLQuery) query).addScalar("dataRecolhimento", StandardBasicTypes.DATE);
		((SQLQuery) query).addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery) query).addScalar("encalhe", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery) query).addScalar("suplementacao", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery) query).addScalar("vendaCE", StandardBasicTypes.LONG);
		((SQLQuery) query).addScalar("dataLancamentoPrevista", StandardBasicTypes.DATE);
		((SQLQuery) query).addScalar("dataRecolhimentoPrevista", StandardBasicTypes.DATE);
		((SQLQuery) query).addScalar("idLancamento", StandardBasicTypes.LONG);
		((SQLQuery) query).addScalar("statusLancamento", StandardBasicTypes.STRING);
		((SQLQuery) query).addScalar("idPeriodo", StandardBasicTypes.LONG);
		((SQLQuery) query).addScalar("origem");
		
		query = buscarParametrosLancamentosParciais(filtro,query);
		
		query.setParameterList("gruposMovimentoEstoqueLancamento", 
				Arrays.asList(GrupoMovimentoEstoque.ENVIO_JORNALEIRO.name(),
							  GrupoMovimentoEstoque.FALTA_DE.name(),
							  GrupoMovimentoEstoque.FALTA_EM.name(),
							  GrupoMovimentoEstoque.SOBRA_DE.name(),
							  GrupoMovimentoEstoque.SOBRA_EM.name(),
							  GrupoMovimentoEstoque.ESTORNO_COMPRA_ENCALHE.name(),
							  GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR.name(),
							  GrupoMovimentoEstoque.ESTORNO_ENVIO_REPARTE.name(),
							  GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE.name(),
							  GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE.name(),
							  GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR.name(),
							  GrupoMovimentoEstoque.COMPRA_ENCALHE.name(),
							  GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR.name(),
							  GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name(),
							  GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE.name(),
							  GrupoMovimentoEstoque.VENDA_ENCALHE.name(),
							  GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR.name()));
				
		query.setParameterList("tipoLancamentoRedistribuicao",Arrays.asList(TipoLancamento.REDISTRIBUICAO.name()));
				
		query.setParameterList("tipoLancamento", Arrays.asList(TipoLancamento.LANCAMENTO.name()));
				
		query.setParameterList("gruposMovimentoEstoqueEncalhe", Arrays.asList(GrupoMovimentoEstoque.ENVIO_ENCALHE.name()));
		
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

		if(filtro.getEdicaoProduto() != null){ 
			hql.append(" and  produtoEdicao.numeroEdicao=:edProduto ");
		}
		
		if(filtro.getIdFornecedor() != null) { 
			hql.append(" and  fornecedor.id=:idFornecedor ");
		}
		
		if(filtro.getDataInicialDate() != null) { 
			hql.append( " and  lancamento.dataLancamentoDistribuidor>=:dtInicial ");
		}
		
		if(filtro.getDataFinalDate() != null) { 
			hql.append( " and  lancamento.dataRecolhimentoDistribuidor<=:dtFinal ");
		}
		
		if(filtro.getStatus() != null) { 
			hql.append(" and  lancamento.status=:status ");
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
	public List<ParcialVendaDTO> obterDetalhesVenda(Date dataLancamento, Date dataRecolhimento, Long idProdutoEdicao, Long idPeriodo) {

        StringBuilder hql = new StringBuilder();

		hql.append(" select cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nome, pessoa.razaoSocial) as nomeCota, ");
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
		hql.append("	join produtoEdicao.lancamentoParcial.periodos periodo ");

		hql.append("	where chamadaEncalhe.dataRecolhimento >= :dataLancamento ");
		hql.append("	and chamadaEncalhe.dataRecolhimento <= :dataRecolhimento ");
		hql.append("	and produtoEdicao.id = :idProdutoEdicao ");
		hql.append("	and periodo.id = :idPeriodo ");

		Query query =  getSession().createQuery(hql.toString());

		query.setParameter("dataLancamento",dataLancamento);
		query.setParameter("dataRecolhimento",dataRecolhimento);
		query.setParameter("idProdutoEdicao",idProdutoEdicao);
		query.setParameter("idPeriodo",idPeriodo);

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
		
		hql.append(" select min(periodo) from PeriodoLancamentoParcial periodo  ")
			.append(" join periodo.lancamentos lancamento join lancamento.produtoEdicao produtoEdicao ")
			.append(" where produtoEdicao.id =:idProdutoEdicao ")
			.append(" and lancamento.tipoLancamento=:tipoLancamento ");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("tipoLancamento", TipoLancamento.LANCAMENTO);
		
		return (PeriodoLancamentoParcial) query.uniqueResult();
	}
	
	public PeriodoLancamentoParcial obterUltimoLancamentoParcial(Long idProdutoEdicao){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select max(periodo) from PeriodoLancamentoParcial periodo  ")
			.append(" join periodo.lancamentos lancamento join lancamento.produtoEdicao produtoEdicao ")
			.append(" where produtoEdicao.id = :idProdutoEdicao ")
			.append(" and lancamento.tipoLancamento = :tipoLancamento");
		
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
	
	@Override
	public Lancamento obterPrimeiroLancamentoNaoJuramentado(Integer numeroPeriodo, Long idLancamentoParcial) {
        
        StringBuilder hql = new StringBuilder();
        
        hql.append(" select lancamento from Lancamento lancamento ");
        hql.append(" left join lancamento.estudo as estudo ");
        hql.append(" left join estudo.estudoCotas as estudoCota ");
        hql.append(" join lancamento.periodoLancamentoParcial as periodoLancamentoParcial ");
        hql.append(" join periodoLancamentoParcial.lancamentoParcial as lancamentoParcial ");
        hql.append(" where (estudoCota.tipoEstudo is null or estudoCota.tipoEstudo <> :tipoEstudo) ");
        hql.append(" and periodoLancamentoParcial.numeroPeriodo = :numeroPeriodo ");
        hql.append(" and lancamentoParcial.id = :idLancamentoParcial ");
        hql.append(" order by lancamento.dataLancamentoDistribuidor ");
        
        Query query = getSession().createQuery(hql.toString());
        
        query.setParameter("numeroPeriodo", numeroPeriodo);
        query.setParameter("idLancamentoParcial", idLancamentoParcial);
        query.setParameter("tipoEstudo", TipoEstudoCota.JURAMENTADO);
        
        query.setMaxResults(1);
        
        return (Lancamento) query.uniqueResult();
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<Lancamento> obterLancamentosParciais(Long idLancamentoParcial) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" select l from ");
		sql.append(" Lancamento l ");
		sql.append(" where l.periodoLancamentoParcial.lancamentoParcial.id = :idLancamentoParcial ");
		sql.append(" and l.tipoLancamento = :tipoLancamento ");
		sql.append(" order by l.dataLancamentoDistribuidor asc ");

		Query query = this.getSession().createQuery(sql.toString());
		
		query.setParameter("idLancamentoParcial", idLancamentoParcial);
		query.setParameter("tipoLancamento", TipoLancamento.LANCAMENTO);

		return query.list();
	}	
}
