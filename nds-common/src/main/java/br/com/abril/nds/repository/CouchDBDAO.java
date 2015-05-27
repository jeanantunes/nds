package br.com.abril.nds.repository;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;

@Repository
public class CouchDBDAO implements CouchDBRepository {
	
	public static final String DB_NAME  =  "correios";
	
	@Autowired
	private CouchDbProperties couchDbProperties;
	
	@SuppressWarnings("unused")
	private CouchDbConnector couchDbConnector;

	public CouchDBDAO() {
		
	}

	@Override
	public CouchDbClient getCouchDBClient(String sufixoNomeDB, boolean isDBDistribuidor) {
		
		return new CouchDbClient(
				isDBDistribuidor ? "db_" + StringUtils.leftPad(sufixoNomeDB, 8, "0") : sufixoNomeDB,
				true,
				couchDbProperties.getProtocol(),
				couchDbProperties.getHost(),
				couchDbProperties.getPort(),
				couchDbProperties.getUsername(),
				couchDbProperties.getPassword()
		);
	}
	
	@Override
	public void initCouchDbClient() throws MalformedURLException {
		HttpClient authenticatedHttpClient = new StdHttpClient.Builder()
                .url(
                		new URL(
                		couchDbProperties.getProtocol(), 
                		couchDbProperties.getHost(), 
                		couchDbProperties.getPort(), 
                		"")
                	)
                .username(couchDbProperties.getUsername())
                .password(couchDbProperties.getPassword())
                .build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(authenticatedHttpClient);
		this.couchDbConnector = dbInstance.createConnector(DB_NAME, true);
				
	}

}