package br.com.abril.nds.service.impl;

import javax.annotation.PostConstruct;

import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.service.IntegracaoService;

/**
 * Implementação do serviço de integração.
 * 
 * @author Discover Technology
 *
 */
@Service
public class IntegracaoServiceImpl implements IntegracaoService {

	private static final String DB_NAME = "db_integracao";
	
	@Autowired
	private CouchDbProperties couchDbProperties;
	
	private CouchDbClient couchDbClient;
	
	/**
	 * Inicializa o cliente.
	 */
	@PostConstruct
	public void initCouchDbClient() {
		
		this.couchDbClient = 
			new CouchDbClient(
				DB_NAME, true, couchDbProperties.getProtocol(), 
					couchDbProperties.getHost(), couchDbProperties.getPort(), 
						couchDbProperties.getUsername(), couchDbProperties.getPassword());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CouchDbClient getCouchDbClient() {
		
		return this.couchDbClient;
	}

}
