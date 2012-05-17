package br.com.abril.nds.integracao.persistence.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.persistence.model.ParametroSistema;

@Component("parametroSistemaDAO")
public class ParametroSistemaHibernateDAO {

	@PersistenceContext
	private EntityManager em;
	
	public String getParametro(String tipoParametro) {
		
		try {
			String sql = "SELECT a.valor FROM ParametroSistema a WHERE a.tipoParametroSistema = :tipoParametro";
			Query query = em.createQuery(sql);
			query.setParameter("tipoParametro", tipoParametro);
			return (String) query.getSingleResult();
		} catch (PersistenceException e) {
			return null;
		}
	}
}
