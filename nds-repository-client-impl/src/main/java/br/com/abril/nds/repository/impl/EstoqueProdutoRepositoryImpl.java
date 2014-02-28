package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroEstoqueProdutosRecolhimento;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoDTO;
import br.com.abril.nds.model.estoque.EstoqueProdutoRecolimentoDTO;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class EstoqueProdutoRepositoryImpl extends AbstractRepositoryModel<EstoqueProduto, Long> implements EstoqueProdutoRespository {

	public EstoqueProdutoRepositoryImpl() {
		super(EstoqueProduto.class);
		
	}

	@Override
	public EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao) {

		Criteria criteria = super.getSession().createCriteria(EstoqueProduto.class, "estoqueProduto");
		
		criteria.createAlias("estoqueProduto.produtoEdicao", "produtoEdicao");
		
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
		
		criteria.setMaxResults(1);
		
		return (EstoqueProduto) criteria.uniqueResult();
	}

	public Long selectForUpdate(Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT E.ID AS id ");
		
		hql.append(" FROM ESTOQUE_PRODUTO E ");
		
		hql.append(" WHERE E.PRODUTO_EDICAO_ID = :idProdutoEdicao FOR UPDATE ");
		
		Query query = this.getSession().createSQLQuery(hql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		((org.hibernate.SQLQuery)query).addScalar("id", StandardBasicTypes.LONG);
		
		List ids = query.list();
	
		if(ids==null || ids.isEmpty()){
			return null;
		}
		
		return (Long) ids.get(0);
		
	}

	
	public EstoqueProduto buscarEstoqueProdutoPorProdutoEdicao(Long idProdutoEdicao) {
		StringBuilder hql = new StringBuilder("select estoqueProduto ");
		hql.append(" from EstoqueProduto estoqueProduto join estoqueProduto.produtoEdicao produtoEdicao ")
		   .append(" where produtoEdicao.id = :idProdutoEdicao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setMaxResults(1);
		
		return (EstoqueProduto) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<EstoqueProdutoDTO> buscarEstoquesProdutos() {
		StringBuilder hql = new StringBuilder("")
			.append("SELECT coalesce(ep.qtde, 0) AS qtde ")
			.append("	, coalesce(ep.qtde_danificado, 0) as qtdeDanificado ")
			.append("	, coalesce(ep.qtde_devolucao_encalhe, 0) as qtdeDevolucaoEncalhe ")
			.append("	, coalesce(ep.qtde_suplementar, 0) as qtdeSuplementar ")
			.append("	, coalesce(rs1.qtde_juramentado, 0) as qtdeJuramentado ")
			.append("	, ep.produto_edicao_id as produtoEdicaoId ")
			.append("from ESTOQUE_PRODUTO ep ")
			.append("left outer join (select produto_edicao_id, sum(qtde) as qtde_juramentado ")
			.append("					from estoque_produto_cota_juramentado ")
			.append("					group by produto_edicao_id) rs1 on ep.PRODUTO_EDICAO_ID=rs1.PRODUTO_EDICAO_ID ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		
		query.addScalar("qtde", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("qtdeDanificado", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("qtdeDevolucaoEncalhe", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("qtdeSuplementar", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("qtdeJuramentado", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("produtoEdicaoId", StandardBasicTypes.LONG);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EstoqueProdutoDTO.class));
		
		return (List<EstoqueProdutoDTO>) query.list();
	}

	@Override
	public Long buscarEstoqueProdutoRecolhimentoCount(FiltroEstoqueProdutosRecolhimento filtro) {
		
		StringBuilder hql = new StringBuilder("select (estoqueProduto.id) ");
		this.getQueryBuscarEstoqueProdutoRecolhimento(hql);
		
		hql.append(" group by produtoEdicao.id ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		this.setParametrosBuscarEstoqueProdutoRecolhimento(query, filtro);
		
		return (long) query.list().size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EstoqueProdutoRecolimentoDTO> buscarEstoqueProdutoRecolhimento(
			FiltroEstoqueProdutosRecolhimento filtro) {
		
		StringBuilder hqlRecolhimentoPDV = new StringBuilder();
		hqlRecolhimentoPDV.append(" coalesce((select sum(conEnc.qtde) ")
		   .append(" from ConferenciaEncalhe conEnc ")
		   .append("	join conEnc.controleConferenciaEncalheCota conConfEncCota ")
		   .append("	join conEnc.produtoEdicao pe ")
		   .append("	where conConfEncCota.dataOperacao = :dataRecolhimento ")
		   .append("	and pe.id = produtoEdicao.id),0) ");
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append(" chamadaEncalhe.sequencia as sequencia, ")
		   .append(" produto.codigo as codigo, ")
		   .append(" produto.nome as produto, ")
		   .append(" produtoEdicao.numeroEdicao as numeroEdicao, ")
		   .append(" coalesce(estoqueProduto.qtde, 0) as lancamento, ")
		   .append(" coalesce(estoqueProduto.qtdeSuplementar, 0) as suplementar, ")
		   .append(" coalesce(estoqueProduto.qtdeDanificado, 0) as danificado, ")
		   .append(" (coalesce(estoqueProduto.qtdeDevolucaoEncalhe, 0) - ")
		   .append(hqlRecolhimentoPDV)
		   .append(" ) as recolhimento, ")
		   .append(hqlRecolhimentoPDV)
		   .append(" as recolhimentoPDV,")
		   .append(" (coalesce(estoqueProduto.qtde, 0) + coalesce(estoqueProduto.qtdeSuplementar, 0) +  ")
		   .append(" coalesce(estoqueProduto.qtdeDanificado, 0) + ")
		   .append(hqlRecolhimentoPDV).append(" + ")
		   .append(" coalesce(estoqueProduto.qtdeDevolucaoEncalhe, 0) - ")
		   .append(hqlRecolhimentoPDV)
		   .append(" )as total ");
		
		this.getQueryBuscarEstoqueProdutoRecolhimento(hql);
		
		PaginacaoVO paginacaoVO = filtro.getPaginacaoVO();
		
		hql.append(" group by produtoEdicao.id ");
		
		if (paginacaoVO != null){
			
			hql.append(" order by ")
			   .append(paginacaoVO.getSortColumn())
			   .append(" ")
			   .append(paginacaoVO.getSortOrder());
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		this.setParametrosBuscarEstoqueProdutoRecolhimento(query, filtro);
		
		if (paginacaoVO != null && paginacaoVO.getPaginaAtual() != null){
			
			query.setFirstResult(paginacaoVO.getPosicaoInicial());
			query.setMaxResults(paginacaoVO.getQtdResultadosPorPagina());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EstoqueProdutoRecolimentoDTO.class));
		
		return query.list();
	}
	
	private void getQueryBuscarEstoqueProdutoRecolhimento(StringBuilder hql){
		
		hql.append(" from EstoqueProduto estoqueProduto ")
		   .append(" join estoqueProduto.produtoEdicao produtoEdicao ")
		   .append(" join produtoEdicao.produto produto ")
		   .append(" join produtoEdicao.chamadaEncalhes chamadaEncalhe ")
		   .append(" join chamadaEncalhe.chamadaEncalheCotas cec ")
		   .append(" where chamadaEncalhe.dataRecolhimento = :dataRecolhimento ")
		   .append(" and cec.postergado = :naoPostergado ");
		   
	}
	
	private void setParametrosBuscarEstoqueProdutoRecolhimento(Query query, 
			FiltroEstoqueProdutosRecolhimento filtro){
		
		query.setParameter("dataRecolhimento", filtro.getDataRecolhimento());
		query.setParameter("naoPostergado", false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Date> obterDatasRecProdutosFechados() {
		
		StringBuilder hql = new StringBuilder("select distinct ");
		hql.append(" lan.dataRecolhimentoPrevista ")
		   .append(" from EstoqueProduto ep ")
		   .append(" join ep.produtoEdicao pre ")
		   .append(" join pre.lancamentos lan ")
		   .append(" join pre.chamadaEncalhes ce")
		   .append(" where lan.status = :statusFechado ")
		   .append(" group by ep.id ")
		   .append(" having (sum(coalesce(ep.qtde,0)) + sum(coalesce(ep.qtdeSuplementar,0)) ")
		   .append(" + sum(coalesce(ep.qtdeDevolucaoEncalhe,0))) != 0 ")
		   .append(" order by lan.dataRecolhimentoPrevista ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("statusFechado", StatusLancamento.FECHADO);
		
		return query.list();
	}
	
	@Override
	public BigInteger buscarQtdEstoquePorProduto(String codigoProduto, List<Long> numeroEdicao) {
		
		StringBuilder hql = new StringBuilder("select sum(coalesce(e.qtde,0)) ");
		hql.append(" from EstoqueProduto e ")
		   .append(" join e.produtoEdicao pe ")
		   .append(" join pe.produto p ")
		   .append(" where p.codigo = :codigoProduto ")
		   .append(" and pe.numeroEdicao in (:numeroEdicao) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("codigoProduto", codigoProduto);
		query.setParameterList("numeroEdicao", numeroEdicao);
		
		return (BigInteger) query.uniqueResult();
	}
	
	
	@Override
	public BigInteger buscarQtdEstoqueProdutoEdicao(Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select e.qtde 				")
		   .append(" from EstoqueProduto e 		")
		   .append(" join e.produtoEdicao pe 	")
		   .append(" where pe.id = :idProdutoEdicao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		return (BigInteger) query.uniqueResult();
	}
	
}