package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.abril.icd.axis.util.Constantes;
import br.com.abril.icd.axis.util.DateUtil;
import br.com.abril.nds.dto.PickingLEDFullDTO;
import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.service.LedModelo4IntegracaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Service
public class LedModelo4IntegracaoServiceImpl implements LedModelo4IntegracaoService{
	
	@Autowired
	private CouchDbProperties couchDbProperties;
	
	private CouchDbClient couchDbClient;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private String db_name = "picking_led";
	
	public CouchDbClient getCouchDB_Client(){
		
		db_name += "_db_"+String.format("%08d",Integer.parseInt(distribuidorService.obter().getCodigoDistribuidorDinap())<=0?
				 							   Integer.parseInt(distribuidorService.obter().getCodigoDistribuidorFC())
			 							   	  :Integer.parseInt(distribuidorService.obter().getCodigoDistribuidorDinap()));
		
		org.lightcouch.CouchDbProperties properties = new org.lightcouch.CouchDbProperties()
				.setDbName(db_name)
				.setCreateDbIfNotExist(true)
				.setProtocol(couchDbProperties.getProtocol())
				.setHost(couchDbProperties.getHost())
				.setPort(couchDbProperties.getPort())
				.setUsername(couchDbProperties.getUsername())
				.setPassword(couchDbProperties.getPassword())
				.setMaxConnections(100)
				.setConnectionTimeout(500);
		
			return new CouchDbClient(properties);
			
	}
	
	@Override
	@Transactional
	public void exportarPickingLED(List<PickingLEDFullDTO> registros, Date dataParametroParaExtracao){
		Gson gson = new Gson();
		JsonArray jA = new JsonArray();
		
		this.couchDbClient = getCouchDB_Client();
		
		for (PickingLEDFullDTO registro : registros) {
			JsonElement jElement = new JsonParser().parse(gson.toJson(registro)); 
			jA.add(jElement);
		}
		
		JsonObject json = new JsonObject();
		
		String dataFormatada = DateUtil.formatarData(dataParametroParaExtracao, Constantes.DATE_PATTERN_PT_BR_COUCH).toString();

		String docName = "pickingLed_"+dataFormatada;
		
		try {
			
			JsonObject jsonDoc = couchDbClient.find(JsonObject.class, docName);
			this.couchDbClient.remove(jsonDoc);
			
		} catch (NoDocumentException e) {

		}
		
		json.addProperty("_id", docName);
		json.add(dataFormatada, jA);
		
		this.couchDbClient.save(json); 
		
	}
	
}
