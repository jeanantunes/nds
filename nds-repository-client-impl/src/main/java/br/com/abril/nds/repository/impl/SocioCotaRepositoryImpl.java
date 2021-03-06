package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.SocioCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.SocioCotaRepository;

@Repository
public class SocioCotaRepositoryImpl extends AbstractRepositoryModel<SocioCota,Long> implements SocioCotaRepository {

	public SocioCotaRepositoryImpl() {
		super(SocioCota.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SocioCota> obterSocioCotaPorIdCota(Long idCota){
		
		Criteria criteria  = getSession().createCriteria(SocioCota.class);
		
		criteria.add(Restrictions.eq("cota.id", idCota));
		
		return criteria.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removerSociosCota(Long idCota){
		
		StringBuilder hql  = new StringBuilder();
		
		hql.append(" Delete from SocioCota where cota.id = :idCota ");
		
		Query q = getSession().createQuery(hql.toString());
		
		q.setParameter("idCota", idCota);
		
		q.executeUpdate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existeSocioPrincipalCota(Long idCota) {
		
		Criteria criteria = getSession().createCriteria(SocioCota.class);
		
		criteria.createAlias("cota", "cota");
		
		criteria.add(Restrictions.eq("cota.id", idCota));
		
		criteria.setProjection(Projections.rowCount());
		
		return (Long) criteria.uniqueResult() > 0;
	}
}
