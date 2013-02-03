package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TipoLicencaMunicipalRepository;

@Repository
public class TipoLicencaMunicipalRepositoryImpl extends AbstractRepositoryModel<TipoLicencaMunicipal,Long> implements TipoLicencaMunicipalRepository {
	
	public TipoLicencaMunicipalRepositoryImpl() {
		super(TipoLicencaMunicipal.class);
	}
	
	public TipoLicencaMunicipal obterTipoLicencaMunicipal(Long codigo){
		
		Criteria criteria = getSession().createCriteria(TipoLicencaMunicipal.class);
		
		criteria.add(Restrictions.eq("codigo",codigo));
		
		return (TipoLicencaMunicipal) criteria.uniqueResult();
	}
}
