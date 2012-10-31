package br.com.abril.nds.integracao.repository.impl;

import javax.persistence.PersistenceException;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.integracao.model.ParametroSistema;
import br.com.abril.nds.integracao.repository.ParametroSistemaRepository;

@Repository
public class ParametroSistemaRepositoryImpl extends AbstractRepositoryModel<ParametroSistema, Long>
		implements ParametroSistemaRepository {



	public ParametroSistemaRepositoryImpl() {
		super(ParametroSistema.class);
		// TODO Auto-generated constructor stub
	}

	public String getParametro(String tipoParametro) {
		
		try {
			String sql = "SELECT a.valor FROM ParametroSistema a WHERE a.tipoParametroSistema = :tipoParametro";
			Query query = getSession().createQuery(sql);
			query.setParameter("tipoParametro", tipoParametro);
			return (String) query.uniqueResult();
		} catch (PersistenceException e) {
			return null;
		}
	}
}
