package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaBaseCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.CotaBaseCotaRepository;

@Repository
public class CotaBaseCotaRepositoryImpl extends AbstractRepositoryModel<CotaBaseCota, Long> implements CotaBaseCotaRepository {

	public CotaBaseCotaRepositoryImpl() {
		super(CotaBaseCota.class);
	}

	@Override
	public Long verificarExistenciaCotaBaseCota(Cota cota) {
		
		StringBuilder hql = new StringBuilder();
		
        hql.append(" select cotaBaseCota ");        
        
        hql.append(" FROM CotaBaseCota as cotaBaseCota ");       
        
        hql.append(" where cotaBaseCota.cota.numeroCota = :numeroCota ");   
        
        Query query =  getSession().createQuery(hql.toString());
        
        query.setParameter("numeroCota", cota.getNumeroCota());        
        
		return (Long) query.uniqueResult();
	}

	
}
