package br.com.abril.nds.integracao.repository.impl;

import javax.persistence.PersistenceException;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.repository.ParametroDistribuidorRepository;
import br.com.abril.nds.model.integracao.ParametroDistribuidor;

@Repository
@Transactional("transactionManager")
public class ParametroDistribuidorRepositoryImpl extends AbstractRepositoryModel<ParametroDistribuidor, Long>
		implements ParametroDistribuidorRepository {

	public ParametroDistribuidorRepositoryImpl() {
		super(ParametroDistribuidor.class);
	}

	@Override
	public ParametroDistribuidor findByCodigoDinapFC(String codigo) {
		try {
			
			Query query  = getSession().createQuery("select o from ParametroDistribuidor o where o.codigoDinap =:codigo or o.codigoFC =:codigo ");
			
			query.setParameter("codigo", codigo);
			query.setMaxResults(1);
			
			return (ParametroDistribuidor) query.uniqueResult();
			
		} catch (PersistenceException e) {
			return null;
		}
	}
}
