package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaBase;
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

	@Override
	public boolean isCotaBaseAtiva(CotaBase cotaBase, Integer[] numerosDeCotasBase) {
		
		StringBuilder hql = new StringBuilder();
		
        hql.append(" SELECT count(cotaBaseCota) ");        
        
        hql.append(" FROM CotaBaseCota as cotaBaseCota ");       
        
        hql.append(" WHERE cotaBaseCota.cotaBase.id = :idCotaBase ");   
        
        hql.append(" AND  cotaBaseCota.ativo = true ");
        
        if (numerosDeCotasBase != null) {
        	hql.append(" AND cota.numeroCota not in (:numerosDeCotasBase)" );
        }
        
        Query query =  getSession().createQuery(hql.toString());
        
        query.setParameter("idCotaBase", cotaBase.getId());
        
        if (numerosDeCotasBase != null) {
        	query.setParameterList("numerosDeCotasBase", numerosDeCotasBase);
        }
        
        Long isAtiva = (Long) query.uniqueResult();
        
		return isAtiva > 0 ? true : false;
	}

	@Override
	public CotaBaseCota desativarCotaBase(CotaBase cotaBase,
			Cota cotaParaDesativar) {
		
		StringBuilder hql = new StringBuilder();
		
        hql.append(" SELECT cotaBaseCota ");        
        
        hql.append(" FROM CotaBaseCota as cotaBaseCota ");       
        
        hql.append(" WHERE cotaBaseCota.cotaBase.id = :idCotaBase ");   
        
        hql.append(" AND  cotaBaseCota.cota.id = :idCotaPataDesativar ");
        
        hql.append(" AND  cotaBaseCota.ativo = true");
        
        Query query =  getSession().createQuery(hql.toString());
        
        query.setParameter("idCotaBase", cotaBase.getId());
        query.setParameter("idCotaPataDesativar", cotaParaDesativar.getId());
        
        return  (CotaBaseCota) query.uniqueResult();
	}

	@Override
	public Long quantidadesDeCotasAtivas(CotaBase cotaBase) {
		
		StringBuilder hql = new StringBuilder();
		
        hql.append(" SELECT count(cotaBaseCota) ");        
        
        hql.append(" FROM CotaBaseCota as cotaBaseCota ");       
        
        hql.append(" WHERE cotaBaseCota.cotaBase.id = :idCotaBase ");   
        
        hql.append(" AND  cotaBaseCota.ativo = true ");
        
        Query query =  getSession().createQuery(hql.toString());
        
        query.setParameter("idCotaBase", cotaBase.getId());
        
        return (Long) query.uniqueResult();
        
	}

}
