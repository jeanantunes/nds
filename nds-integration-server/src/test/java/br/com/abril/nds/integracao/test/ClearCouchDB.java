package br.com.abril.nds.integracao.test;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightcouch.CouchDbClient;
import org.lightcouch.View;
import org.lightcouch.ViewResult;
import org.lightcouch.ViewResult.Rows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.integracao.fileimporter.StartBatch;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager="transactionManager")
@ContextConfiguration(locations={StartBatch.SPRING_FILE_LOCATION})
public class ClearCouchDB {

	@Autowired
	private CouchDbProperties couchDbProperties;
	

	@Test
	public void limparCouchDB() {
		
		CouchDbClient couchDbClient = getCouchDBClient();
		
		View view = couchDbClient.view("importacao/porTipoDocumento");
		
		view.key("EMS0135");
		view.limit(couchDbProperties.getBachSize());
		view.includeDocs(true);
		ViewResult<String, Void, ?> result = view.queryView(String.class, Void.class, null);

		for (@SuppressWarnings("rawtypes") Rows row: result.getRows()) {
			
			IntegracaoDocument doc = (IntegracaoDocument) row.getDoc(); 
		
			couchDbClient.remove(doc);
			
		}

	}
	
	private CouchDbClient getCouchDBClient() {
		
		return new CouchDbClient(
				"db_" + StringUtils.leftPad("6248116", 8, "0"),
				true,
				couchDbProperties.getProtocol(),
				couchDbProperties.getHost(),
				couchDbProperties.getPort(),
				couchDbProperties.getUsername(),
				couchDbProperties.getPassword()
		);
	}

}
