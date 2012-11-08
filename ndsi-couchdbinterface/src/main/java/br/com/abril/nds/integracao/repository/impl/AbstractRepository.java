package br.com.abril.nds.integracao.repository.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;

public class AbstractRepository {

	@Autowired
	private SessionFactory sessionFactory;
		
	@Autowired
	private SessionFactory sessionFactoryIcd;
		
	@Autowired
	private CouchDbProperties couchDbProperties;

	public AbstractRepository() {
		
	}

	protected Session getSession() {		
		return sessionFactory.getCurrentSession();
	}
	
	protected Session getSessionIcd() {		
		return sessionFactoryIcd.getCurrentSession();
	}
	
	/**
	 * Retorna o client para o CouchDB na database correspondente ao distribuidor.
	 * 
	 * @return client
	 */
	protected CouchDbClient getCouchDBClient(String codDistribuidor) {
		
		return new CouchDbClient(
				"db_" + StringUtils.leftPad(codDistribuidor, 8, "0"),
				true,
				couchDbProperties.getProtocol(),
				couchDbProperties.getHost(),
				couchDbProperties.getPort(),
				couchDbProperties.getUsername(),
				couchDbProperties.getPassword()
		);
	}
}