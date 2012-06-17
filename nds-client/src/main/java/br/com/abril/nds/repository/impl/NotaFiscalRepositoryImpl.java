package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.repository.NotaFiscalRepository;

@Repository
public class NotaFiscalRepositoryImpl extends AbstractRepository<NotaFiscal, Long> implements NotaFiscalRepository {
	
	public NotaFiscalRepositoryImpl() {
		super(NotaFiscal.class);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.NotaFiscalRepository#obterListaNotasFiscaisPor(br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NotaFiscal> obterListaNotasFiscaisPor(
			StatusProcessamentoInterno statusProcessamentoInterno) {
		
		Criteria criteria = getSession().createCriteria(NotaFiscal.class);
		
		criteria.add(Restrictions.eq("statusProcessamentoInterno", statusProcessamentoInterno));
		
		return criteria.list();
	}
	
}
