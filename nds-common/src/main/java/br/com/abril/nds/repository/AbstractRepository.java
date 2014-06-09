package br.com.abril.nds.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * Implementação das operações básicas do repositório
 * 
 * @author francisco.garcia
 *
 * @param <T> tipo em manipulação pelo repositório 
 * @param <K> tipo do identificador do repositório
 */
public abstract class AbstractRepository extends CouchDBRepositoryImpl {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	protected Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			
		}
		return null;
		//return sessionFactory.openSession();
	}
	
}
