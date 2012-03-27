package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.repository.DividaRepository;

@Repository
public class DividaRepositoryImpl extends AbstractRepository<Divida, Long> implements
		DividaRepository {

	public DividaRepositoryImpl() {
		super(Divida.class);
	}
	
	public Divida obterUltimaDividaPorCota(Long idCota){
		Criteria criteria = this.getSession().createCriteria(Divida.class);
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.setProjection(Projections.max("data"));
		
		return (Divida) criteria.uniqueResult();
	}
}