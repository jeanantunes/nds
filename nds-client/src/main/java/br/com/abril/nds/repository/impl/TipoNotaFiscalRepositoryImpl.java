package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;

@Repository
public class TipoNotaFiscalRepositoryImpl extends AbstractRepository<TipoNotaFiscal, Long> 
										  implements TipoNotaFiscalRepository {

	public TipoNotaFiscalRepositoryImpl() {
		super(TipoNotaFiscal.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoNotaFiscal> obterTiposNotasFiscais() {

		String hql = " from TipoNotaFiscal tipoNotaFiscal ";
		
		Query query = getSession().createQuery(hql);
		
		return query.list();
	}
}
