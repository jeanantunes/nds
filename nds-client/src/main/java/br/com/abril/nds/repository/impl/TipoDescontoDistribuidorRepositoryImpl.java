package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.TipoDescontoDistribuidor;
import br.com.abril.nds.repository.TipoDescontoDistribuidorRepository;

@Repository
public class TipoDescontoDistribuidorRepositoryImpl extends AbstractRepositoryModel<TipoDescontoDistribuidor,Long> implements	TipoDescontoDistribuidorRepository {

	public TipoDescontoDistribuidorRepositoryImpl() {
		super(TipoDescontoDistribuidor.class);
	}

	@Override
	public int obterSequencial() {		
		StringBuilder hql = new StringBuilder();

		hql.append(
				" SELECT MAX(tipo.sequencial) FROM TipoDescontoDistribuidor as tipo");

		Query query = getSession().createQuery(hql.toString());
		
		return (Integer) ((query.uniqueResult() == null) ? 0 : query.uniqueResult()); 
	}

}
