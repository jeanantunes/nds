package br.com.abril.nds.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.abril.nds.dto.CotaCouchDTO;
import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.service.ExporteCouch;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Service
public class ExporteCouchImpl implements ExporteCouch {

	@Autowired
	private CouchDbProperties couchDbProperties;

	private CouchDbClient couchDbClient;

	@Autowired
	private DistribuidorService distribuidorService;

	public void exportar(List<CotaCouchDTO> listaReparte, String nomeEntidadeIntegrada) {
		Gson gson = new Gson();
		JsonArray jsonArray = new JsonArray();
	

			this.couchDbClient = getCouchDBClient();

			for (CotaCouchDTO reparte : listaReparte) {
				JsonElement jElement = new JsonParser().parse(gson.toJson(reparte));
				jsonArray.add(jElement);
			}

			JsonObject json = new JsonObject();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date data = listaReparte.get(0).getDataMovimento();
			String docName = nomeEntidadeIntegrada+"_" + simpleDateFormat.format(data);
			
		try {
			JsonObject jsonDoc = couchDbClient.find(JsonObject.class, docName);
			this.couchDbClient.remove(jsonDoc);
		} catch (NoDocumentException e) {

		}

			json.addProperty("_id", docName);
			json.add(simpleDateFormat.format(data), jsonArray);

			this.couchDbClient.save(json);
	}

	private CouchDbClient getCouchDBClient() {
		String db_name = "picking_led";

		db_name += "_db_" + String.format("%08d",
				Integer.parseInt(distribuidorService.obter().getCodigoDistribuidorDinap()) <= 0
						? Integer.parseInt(distribuidorService.obter().getCodigoDistribuidorFC())
						: Integer.parseInt(distribuidorService.obter().getCodigoDistribuidorDinap()));

		org.lightcouch.CouchDbProperties properties = new org.lightcouch.CouchDbProperties().setDbName(db_name)
				.setCreateDbIfNotExist(true).setProtocol(couchDbProperties.getProtocol())
				.setHost(couchDbProperties.getHost()).setPort(couchDbProperties.getPort())
				.setUsername(couchDbProperties.getUsername()).setPassword(couchDbProperties.getPassword())
				.setMaxConnections(100).setConnectionTimeout(500);

		return new CouchDbClient(properties);

	}

}
