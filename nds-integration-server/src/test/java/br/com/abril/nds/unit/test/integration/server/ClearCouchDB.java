package br.com.abril.nds.unit.test.integration.server;

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
import br.com.abril.nds.integracao.model.canonic.EMS0127Input;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager="transactionManager")
@ContextConfiguration(locations="file:src/test/resources/spring/applicationContext-ndsi-test.xml")
public class ClearCouchDB {

	@Autowired
	private CouchDbProperties couchDbProperties;

	@Test
	public void limparCouchDB() {
		
		try {
			CouchDbClient couchDbClient = getCouchDBClient();
			
			View view = couchDbClient.view("importacao/porTipoDocumento");
			
			view.key("EMS0135");
			view.limit(couchDbProperties.getBachSize());
			view.includeDocs(true);
			
			ViewResult<String, Void, ?> result = view.queryView(String.class, Void.class, EMS0127Input.class);
	
			for (@SuppressWarnings("rawtypes") Rows row: result.getRows()) {
				
				couchDbClient.remove(row.getDoc());
			}
		} catch(Exception e) {
			e.printStackTrace();
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