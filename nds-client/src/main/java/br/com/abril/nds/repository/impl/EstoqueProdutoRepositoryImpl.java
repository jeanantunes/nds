package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.repository.EstoqueProdutoRespository;

@Repository
public class EstoqueProdutoRepositoryImpl extends AbstractRepository<EstoqueProduto, Long> implements EstoqueProdutoRespository{

	public EstoqueProdutoRepositoryImpl() {
		super(EstoqueProduto.class);
		
	}

	@Override
	public EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao) {

		if(	idProdutoEdicao == null ) {
			throw new NullPointerException();
		}
		
		Criteria criteria = super.getSession().createCriteria(EstoqueProduto.class);
		
		criteria.add(Restrictions.eq("id", idProdutoEdicao));
		
		criteria.setMaxResults(1);
		
		return (EstoqueProduto) criteria.uniqueResult();
	}
	
	public EstoqueProduto buscarEstoqueProdutoPorProdutoEdicao(Long idProdutoEdicao){
		StringBuilder hql = new StringBuilder("select estoqueProduto ");
		hql.append(" from EstoqueProduto estoqueProduto, ProdutoEdicao produtoEdicao ")
		   .append(" where estoqueProduto.produtoEdicao.id = produtoEdicao.id ")
		   .append(" and produtoEdicao.id = :idProdutoEdicao");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setMaxResults(1);
		
		return (EstoqueProduto) query.uniqueResult();
	}

}
