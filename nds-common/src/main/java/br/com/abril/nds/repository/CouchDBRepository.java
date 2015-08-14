package br.com.abril.nds.repository;

import java.net.MalformedURLException;

import org.lightcouch.CouchDbClient;

public interface CouchDBRepository {

	/**
	 * Retorna o client para o CouchDB na database correspondente ao distribuidor.
	 * @param isDBDistribuidor TODO
	 * 
	 * @return client
	 */
	public abstract CouchDbClient getCouchDBClient(String sufixoNomeDB, boolean isDBDistribuidor);
	public abstract CouchDbClient getCouchDBClient(String sufixoNomeDB, boolean isDBDistribuidor,int timeout);

	//@PostConstruct
	public abstract void initCouchDbClient() throws MalformedURLException;
	

}