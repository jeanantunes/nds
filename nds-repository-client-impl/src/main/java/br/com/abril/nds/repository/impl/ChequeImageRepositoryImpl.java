package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ChequeImage;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ChequeImageRepository;

/**
 * 
 * @author Diego Fernandes
 *
 */
@Repository
public class ChequeImageRepositoryImpl extends AbstractRepositoryModel<ChequeImage, Long>
		implements ChequeImageRepository {

	public ChequeImageRepositoryImpl() {
		super(ChequeImage.class);
	}
	
	@Override
	public byte[] getImageCheque(long idCheque){
		Criteria criteria = getSession().createCriteria(ChequeImage.class);
		
		criteria.add(Restrictions.eq("cheque.id", idCheque));
		criteria.setProjection(Projections.property("imagem"));
		return (byte[]) criteria.uniqueResult();
	}
	
	
	@Override
	public ChequeImage get(long idCheque){
		Criteria criteria = getSession().createCriteria(ChequeImage.class);		
		criteria.add(Restrictions.eq("cheque.id", idCheque));
		return (ChequeImage) criteria.uniqueResult();
	}
}
