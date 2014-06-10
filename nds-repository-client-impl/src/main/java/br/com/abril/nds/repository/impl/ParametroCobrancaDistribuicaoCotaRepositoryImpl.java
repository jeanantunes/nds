package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ParametroCobrancaDistribuicaoCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ParametroCobrancaDistribuicaoCotaRepository;

@Repository
public class ParametroCobrancaDistribuicaoCotaRepositoryImpl 
		extends AbstractRepositoryModel<ParametroCobrancaDistribuicaoCota,Long> 
		implements ParametroCobrancaDistribuicaoCotaRepository {

	public ParametroCobrancaDistribuicaoCotaRepositoryImpl() {
		super(ParametroCobrancaDistribuicaoCota.class);
	}
	
	@Override
	public ParametroCobrancaDistribuicaoCota obterParametroPorCota(Long idCota) {
		
		Criteria criteria = getSession().createCriteria(ParametroCobrancaDistribuicaoCota.class);
		
		criteria.createAlias("cota", "cota");
		
		criteria.add(Restrictions.eq("cota.id",idCota));
		
		criteria.setMaxResults(1);
		
		return (ParametroCobrancaDistribuicaoCota) criteria.uniqueResult();
	}
}