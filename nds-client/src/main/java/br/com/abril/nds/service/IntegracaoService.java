package br.com.abril.nds.service;

import org.lightcouch.CouchDbClient;

/**
 * Interface para serviços de integração.
 * 
 * @author Discover Technology
 *
 */
public interface IntegracaoService {
	
	/**
	 * Obtém o cliente para acesso ao CouchDB.
	 * 
	 * @return {@link CouchDbClient}
	 */
	CouchDbClient getCouchDbClient();

}
