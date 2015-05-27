package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.CFOPRepository;

@Repository
public class CFOPRepositoryImpl  extends AbstractRepositoryModel<CFOP, Long> implements CFOPRepository {

	/**
	 * Construtor.
	 */
	public CFOPRepositoryImpl() {
		
		super(CFOP.class);
	}

	@Override
	public CFOP obterPorCodigo(String codigo) {
		Criteria criteria =  getSession().createCriteria(CFOP.class);	

		criteria.add(Restrictions.eq("codigo", codigo));
		
		return (CFOP) criteria.uniqueResult();
	}
}
