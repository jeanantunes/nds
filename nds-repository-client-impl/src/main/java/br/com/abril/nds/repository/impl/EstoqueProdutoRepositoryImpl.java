package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoDTO;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstoqueProdutoRespository;

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
	
	public EstoqueProduto buscarEstoqueProdutoPorProdutoEdicao(Long idProdutoEdicao) {
		StringBuilder hql = new StringBuilder("select estoqueProduto ");
		hql.append(" from EstoqueProduto estoqueProduto, ProdutoEdicao produtoEdicao ")
		   .append(" where estoqueProduto.produtoEdicao.id = produtoEdicao.id ")
		   .append(" and produtoEdicao.id = :idProdutoEdicao");
		
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
	
}
