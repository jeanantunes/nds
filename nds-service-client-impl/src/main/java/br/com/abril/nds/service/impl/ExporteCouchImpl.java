package br.com.abril.nds.service.impl;

import java.util.List;

import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import br.com.abril.nds.dto.CotaConsignadaCouchDTO;
import br.com.abril.nds.dto.CotaCouchDTO;
import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.service.ExporteCouch;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Service
public class ExporteCouchImpl implements ExporteCouch {

	@Autowired
	private CouchDbProperties couchDbProperties;

	private CouchDbClient couchDbClient;
	
	private static String LANCAMENTO_RECOLHIMENTO_COUCH ="db_carga_tpj_lancamentos_recolhimentos";
	private static String COTAS_CONSIGNADO_COUCH ="db_carga_tpj_cota_consignada";

	public void exportarLancamentoRecolhimento(List<CotaCouchDTO> listaReparte, String nomeEntidadeIntegrada) {
		String data = listaReparte.get(0).getDataMovimento();
		this.couchDbClient = getCouchDBClient(LANCAMENTO_RECOLHIMENTO_COUCH);
		String docName = nomeEntidadeIntegrada+"_" + data+"_"+listaReparte.get(0).getCodigoDistribuidor();
		try {
			JsonObject jsonDoc = couchDbClient.find(JsonObject.class, docName);
			this.couchDbClient.remove(jsonDoc);
		} catch (NoDocumentException e) {

		}
		for (CotaCouchDTO reparte : listaReparte) {
			reparte.set_id(docName);
			this.couchDbClient.save(reparte);
		}
	
		if (couchDbClient != null) {
			couchDbClient.shutdown();
		}	
	}

	private CouchDbClient getCouchDBClient(String dbBanco) {
		org.lightcouch.CouchDbProperties properties = new org.lightcouch.CouchDbProperties().setDbName(dbBanco)
				.setCreateDbIfNotExist(true).setProtocol(couchDbProperties.getProtocol())
				.setHost(couchDbProperties.getHost()).setPort(couchDbProperties.getPort())
				.setUsername(couchDbProperties.getUsername()).setPassword(couchDbProperties.getPassword())
				.setMaxConnections(100).setConnectionTimeout(500);

		return new CouchDbClient(properties);

	}

	@Override
	public void exportarCotaConsignada(CotaConsignadaCouchDTO cotaConsignada) {
		couchDbClient = getCouchDBClient(COTAS_CONSIGNADO_COUCH);
		String docName = "consignado_"+cotaConsignada.getCotaConsignadaDetalhes().get(0).getNumeroCota()+"_"+cotaConsignada.getCotaConsignadaDetalhes().get(0).getDistribuidor();
		try {
			JsonObject jsonDoc = couchDbClient.find(JsonObject.class, docName);
			this.couchDbClient.remove(jsonDoc);
		} catch (NoDocumentException e) {

		}
		cotaConsignada.set_id(docName);
		this.couchDbClient.save(cotaConsignada);
			
		if (couchDbClient != null) {
			couchDbClient.shutdown();
		}	
	}

}
