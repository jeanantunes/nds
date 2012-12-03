package br.com.abril.nds.integracao.test;

import org.junit.Test;
import org.lightcouch.Changes;
import org.lightcouch.ChangesResult;
import org.lightcouch.CouchDbClient;
import org.lightcouch.View;
import org.lightcouch.ViewResult;

import com.google.gson.JsonObject;

import br.com.abril.nds.integracao.model.canonic.EMS0111Input;


public class TestLightCouch {
	
	private CouchDbClient getCouchDbClientInstance(String codigoDistribuidor) {
		
		return new CouchDbClient("db_" + codigoDistribuidor,
				true,
				"http",
				"localhost",
				5984,
				"admin",
				"admin"
		);
	}
	
	@Test
	public void TestView() {
		
		try {
			
			CouchDbClient client = this.getCouchDbClientInstance("0548008");
			View view = client.view("importacao/porTipoDocumento");
			view.key("EMS0111");
			
			ViewResult<String, ?, Void> result = view.queryView(String.class, EMS0111Input.class, Void.class);
			
			EMS0111Input deserialized = (EMS0111Input) result.getRows().get(0).getValue();
			
			client.shutdown();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testChanges() {
		
		try {
			
			CouchDbClient client = this.getCouchDbClientInstance("0548008");
			
			Changes changes = client.changes();
			//changes.filter("importacao/porTipoDocumento");
			changes.since("0");
			changes.includeDocs(true);
			
			ChangesResult result = changes.getChanges();
			JsonObject json = result.getResults().get(0).getDoc();
			
			
			
			client.shutdown();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Test
	public void TestSave() {
		
		try {
		
			CouchDbClient client = new CouchDbClient();
			/*
			IntegracaoDocument<PDV> doc = new IntegracaoDocument<PDV>();
			doc.set_id("idTeste1");
			doc.setTipoDocumento("EMS0111");
			doc.setDataHoraExtracao(new Date());
			doc.setDados(this.initPDV());
			
			client.save(doc);
			*/
			client.shutdown();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TesteFind() {
	
		try {
			
			CouchDbClient client = new CouchDbClient();
			/*
			IntegracaoDocument doc = client.find(IntegracaoDocument.class, "idTeste1");
			
			Gson gson = new Gson();
			
			PDV pdv = gson.fromJson(doc.getDados(), PDV.class);
			System.out.println(pdv);
			*/
			client.shutdown();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
