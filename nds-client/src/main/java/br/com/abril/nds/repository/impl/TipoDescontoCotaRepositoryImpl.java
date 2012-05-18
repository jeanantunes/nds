package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.repository.TipoDescontoCotaRepository;

@Repository
public class TipoDescontoCotaRepositoryImpl extends AbstractRepository<TipoDescontoCota,Long> implements TipoDescontoCotaRepository {

	public TipoDescontoCotaRepositoryImpl() {
		super(TipoDescontoCota.class);		 
	}

	@Override
	public int obterSequencial() {
		StringBuilder hql = new StringBuilder();

		hql.append(
				" SELECT MAX(tipo.sequencial) FROM TipoDescontoCota as tipo");

		Query query = getSession().createQuery(hql.toString());

		return (Integer) query.uniqueResult();
	}

	

}
