package br.com.abril.nds.integracao.repository.impl;

import javax.persistence.PersistenceException;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.integracao.repository.ParametroSistemaRepository;
import br.com.abril.nds.model.integracao.ParametroSistema;

@Repository
@Transactional("transactionManager")
public class ParametroSistemaRepositoryImpl extends AbstractRepositoryModel<ParametroSistema, Long>
		implements ParametroSistemaRepository {

	public ParametroSistemaRepositoryImpl() {
		super(ParametroSistema.class);
	}

	public String getParametro(String tipoParametro) {

		try {
			
			String sql = "SELECT a.valor FROM ParametroSistema a WHERE a.tipoParametroSistema = :tipoParametro";
			Query query = getSession().createQuery(sql);
			query.setParameter("tipoParametro", TipoParametroSistema.valueOf(tipoParametro));
			return (String) query.uniqueResult();
			
		} catch (PersistenceException e) {
			return null;
		}
	}
}
