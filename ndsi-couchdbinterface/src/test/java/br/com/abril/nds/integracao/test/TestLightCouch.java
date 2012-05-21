package br.com.abril.nds.integracao.test;

import org.junit.Test;
import org.lightcouch.CouchDbClient;


public class TestLightCouch {

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
