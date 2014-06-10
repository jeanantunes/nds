package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.ParametroEmissaoNotaFiscal;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ParametroEmissaoNotaFiscalRepository;

@Repository
public class ParametroEmissaoNotaFiscalRepositoryImpl extends
		AbstractRepositoryModel<ParametroEmissaoNotaFiscal, Long> implements ParametroEmissaoNotaFiscalRepository {

	/**
	 * Construtor padrão.
	 */
	public ParametroEmissaoNotaFiscalRepositoryImpl() {
		super(ParametroEmissaoNotaFiscal.class);
	}
	
	public ParametroEmissaoNotaFiscal obterParametroEmissaoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal) {
	
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select p 							  		");		
		hql.append(" from ParametroEmissaoNotaFiscal p 	  		");
		hql.append(" where p.grupoNotaFiscal = :grupoNotaFiscal	");
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("grupoNotaFiscal", grupoNotaFiscal);
		
		return (ParametroEmissaoNotaFiscal) query.uniqueResult();
		
	}
	
	
}
