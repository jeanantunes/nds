package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;

@Repository
public class EstoqueProdutoCotaRepositoryImpl extends AbstractRepository<EstoqueProdutoCota, Long> implements EstoqueProdutoCotaRepository{

	public EstoqueProdutoCotaRepositoryImpl() {
		super(EstoqueProdutoCota.class);
	}

	@Override
	public EstoqueProdutoCota buscarEstoquePorProdutoECota(Long idProdutoEdicao, Long idCota) {

		if(	idProdutoEdicao == null ) {
			throw new NullPointerException();
		}
		
		Criteria criteria = super.getSession().createCriteria(EstoqueProdutoCota.class);
		
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
		criteria.add(Restrictions.eq("cota.id", idCota));
				
		criteria.setMaxResults(1);
		
		return (EstoqueProdutoCota) criteria.uniqueResult();
	}
}
