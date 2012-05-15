package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.repository.CotaGarantiaRepository;

/**
 * 
 * @author Diego Fernandes
 *
 */
@Repository
public class CotaGarantiaRepositoryImpl extends AbstractRepository<CotaGarantia, Long> implements CotaGarantiaRepository {

	public CotaGarantiaRepositoryImpl() {
		super(CotaGarantia.class);
	}

	@Override
	public CotaGarantia getByCota(Long idCota) {
		Criteria criteria = getSession().createCriteria(CotaGarantia.class);
		
		criteria.add(Restrictions.eq("cota.id", idCota));
		return (CotaGarantia) criteria.uniqueResult();
	}

	@Override
	public void deleteListaImoveis(Long idGarantia) {
		
		Query query = getSession().createSQLQuery(" DELETE FROM IMOVEL WHERE GARANTIA_ID = :idGarantia ");
		query.setParameter("idGarantia", idGarantia).executeUpdate();
	}

}
