package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
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

}
