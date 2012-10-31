package br.com.abril.nds.integracao.repository.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;

public class AbstractRepository {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CouchDbProperties couchDbProperties;

	public AbstractRepository() {
		
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
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