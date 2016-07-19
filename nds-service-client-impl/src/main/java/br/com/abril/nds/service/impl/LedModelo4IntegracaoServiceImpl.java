package br.com.abril.nds.service.impl;

import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;

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

@Service
public class LedModelo4IntegracaoServiceImpl implements LedModelo4IntegracaoService{
	
	private static final String DB_NAME = "picking_led";
	
	@Autowired
	private CouchDbProperties couchDbProperties;
	
	private CouchDbClient couchDbClient;
	
	@PostConstruct
	public void initCouchDbClient() {
		
		org.lightcouch.CouchDbProperties properties = new org.lightcouch.CouchDbProperties()
			.setDbName(DB_NAME)
			.setCreateDbIfNotExist(true)
			.setProtocol(couchDbProperties.getProtocol())
			.setHost(couchDbProperties.getHost())
			.setPort(couchDbProperties.getPort())
			.setUsername(couchDbProperties.getUsername())
			.setPassword(couchDbProperties.getPassword())
			.setMaxConnections(100)
			.setConnectionTimeout(500);
	
		this.couchDbClient = new CouchDbClient(properties);

	}
	
	@Override
	@Transactional
	public void exportarPickingLED(List<PickingLEDFullDTO> registros){
		Gson gson = new Gson();
		JsonArray jA = new JsonArray();
		
		for (PickingLEDFullDTO registro : registros) {
			JsonElement jElement = new JsonParser().parse(gson.toJson(registro)); 
			jA.add(jElement);
		}
		
		JsonObject json = new JsonObject();
		
		String dataFormatada = DateUtil.formatarData(Calendar.getInstance().getTime(), Constantes.DATE_PATTERN_PT_BR_COUCH).toString();

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
	
	/*
	 * 
	@Transactional
	@Override
	public String importarCotas(){

		Date dataOperacaoDistribuidor = distribuidorService.obterDataOperacaoDistribuidor();
		
		String dataFormatada = DateUtil.formatarData(dataOperacaoDistribuidor, Constantes.DATE_PATTERN_PT_BR_FOR_FILE);
		
		String docName = "cotasExportadas_"+dataFormatada;
		JsonObject jsonDoc = new JsonObject();
		
		try {
			jsonDoc = couchDbClient.find(JsonObject.class, docName);
		} catch (NoDocumentException e) {

		}
		
		if(jsonDoc == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há cotas para importação.");
		}
		
		Gson gson = new Gson();

		JsonArray jaCotas = jsonDoc.getAsJsonArray(dataFormatada);
		
		List<CotaImportadaConsolidador> cotas = new ArrayList<>(); 
		
		if(jaCotas == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há cotas para importação.");
		}
		
		for (JsonElement jsonElement : jaCotas) {
			CotaImportadaConsolidador cota = gson.fromJson(jsonElement, CotaImportadaConsolidador.class);
			cotas.add(cota);
		}
		
		// Refatorar para update/insert 
		/*
		 * INSERT INTO table (id, name, age) VALUES(1, "A", 19) ON DUPLICATE KEY UPDATE    
			name=VALUES(name), age=VALUES(age)
		 */
	/*
		if(!cotas.isEmpty() && cotas.size() > 1){
			this.consolidadorCotaRepositoy.deletarCotasImportadas();

			for (CotaImportadaConsolidador cota : cotas) {
				this.consolidadorCotaRepositoy.adicionar(cota);
			}
		}else{
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há cotas para importação.");
		}
		
		return cotas.size() + " - Cotas importadas com sucesso!";
	}
	 */

	
}
