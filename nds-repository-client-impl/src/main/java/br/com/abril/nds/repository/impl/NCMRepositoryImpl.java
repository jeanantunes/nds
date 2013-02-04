package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.NCMRepository;

@Repository
public class NCMRepositoryImpl extends AbstractRepositoryModel<NCM,Long> implements NCMRepository  {

	/**
	 * Construtor padrão
	 */
	public NCMRepositoryImpl() {
		super(NCM.class);
	}

	/**
	 * Obtem NCM por código
	 * @param codigoNcm
	 * @return NCM
	 */
	@Override
	public NCM obterPorCodigo(Long codigoNcm) {
        Criteria criteria = getSession().createCriteria(NCM.class);
		
		criteria.add(Restrictions.eq("codigo", codigoNcm));
		
		NCM ncm = (NCM) criteria.uniqueResult();
		
		return ncm;
	}

	
}
