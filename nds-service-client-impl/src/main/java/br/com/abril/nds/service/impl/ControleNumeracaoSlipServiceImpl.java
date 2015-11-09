package br.com.abril.nds.service.impl;

import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.model.TipoSlip;
import br.com.abril.nds.model.movimentacao.ControleNumeracaoSlip;
import br.com.abril.nds.repository.ControleNumeracaoSlipRepository;
import br.com.abril.nds.service.ControleNumeracaoSlipService;
import br.com.abril.nds.service.integracao.DistribuidorService;

import com.google.gson.JsonObject;

@Service
public class ControleNumeracaoSlipServiceImpl implements ControleNumeracaoSlipService {

	 private static final Logger LOGGER = LoggerFactory.getLogger(ControleNumeracaoSlipServiceImpl.class);
	 
	@Autowired
	private ControleNumeracaoSlipRepository controleNumeracaoSlipRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CouchDbProperties couchDbProperties;
	
	@Transactional
	public   Long obterProximoNumeroSlip(TipoSlip tipoSlip) {
		
		return obterProximoNumeroSlipCouch(tipoSlip);
		/*
		ControleNumeracaoSlip controleNumeracaoSlip = controleNumeracaoSlipRepository.obterControleNumeracaoSlip(tipoSlip);
		
		if(controleNumeracaoSlip == null) {
			
			controleNumeracaoSlip = new ControleNumeracaoSlip();
			controleNumeracaoSlip.setTipoSlip(tipoSlip);
			controleNumeracaoSlip.setProximoNumeroSlip(1L);
			
			controleNumeracaoSlipRepository.adicionar(controleNumeracaoSlip);
			return 1L;
		}
		
		Long numeroSlip = controleNumeracaoSlip.getProximoNumeroSlip();
		
		if(numeroSlip == null) {
			throw new IllegalStateException("Controle de Numeração de Slip não encontrado");
		}
		
		//controleNumeracaoSlip.setProximoNumeroSlip((numeroSlip + 1L));
		
		controleNumeracaoSlipRepository.alterarPorId(controleNumeracaoSlip.getId(),numeroSlip+ 1L);
		controleNumeracaoSlipRepository.flush();
		return numeroSlip;
		*/
		
	}
	
	
	public  synchronized Long obterProximoNumeroSlipCouch(TipoSlip tipoSlip) {
			
	
		CouchDbClient couchDbClient=null;
		JsonObject jsonDoc;
		String docName = tipoSlip.toString();
		long proximoNumero = 1L;
		try {
			
		
			couchDbClient=getCouchDbClient();
			
			 jsonDoc = couchDbClient.find(JsonObject.class, docName);
		
			proximoNumero=jsonDoc.get("proximoNumero").getAsLong();
			jsonDoc.remove("proximoNumero");
			proximoNumero++;
			
			jsonDoc.addProperty("proximoNumero",proximoNumero );
			
			jsonDoc.addProperty("revision", "1");
			
			couchDbClient.update(jsonDoc);
			
			
		} catch (Exception e) {
		 try {
			jsonDoc = new JsonObject();
			jsonDoc.addProperty("_id", docName);
			jsonDoc.addProperty("revision", "1");
			ControleNumeracaoSlip controleNumeracaoSlip = controleNumeracaoSlipRepository.obterControleNumeracaoSlip(tipoSlip);
			
			if(controleNumeracaoSlip == null) {
				
				controleNumeracaoSlip = new ControleNumeracaoSlip();
				controleNumeracaoSlip.setTipoSlip(tipoSlip);
				controleNumeracaoSlip.setProximoNumeroSlip(1L);
				
				controleNumeracaoSlipRepository.adicionar(controleNumeracaoSlip);
				proximoNumero= 1L;
			} else {
				proximoNumero = controleNumeracaoSlip.getProximoNumeroSlip();
			  }
			jsonDoc.addProperty("proximoNumero",proximoNumero );
			couchDbClient.save(jsonDoc);
		 } catch (Exception ee ) {
			 LOGGER.error("Erro obtendo proximo numero slip couchdb",ee);
		 }
		} finally {
		if ( couchDbClient != null )
		  couchDbClient.shutdown();
		}
		return proximoNumero;
		
		
	}
	
	
	

private static final String DB_NAME = "controle_slip";

	public CouchDbClient getCouchDbClient() {
		CouchDbClient couchDbClient;
		org.lightcouch.CouchDbProperties properties = new org.lightcouch.CouchDbProperties()
			.setDbName(DB_NAME+"_db_"+
					
					 String.format("%08d",Integer.parseInt(distribuidorService.obter().getCodigoDistribuidorDinap())<=0?
							 Integer.parseInt(distribuidorService.obter().getCodigoDistribuidorFC())
							 :Integer.parseInt(distribuidorService.obter().getCodigoDistribuidorDinap())))
			.setCreateDbIfNotExist(true)
			.setProtocol(couchDbProperties.getProtocol())
			.setHost(couchDbProperties.getHost())
			.setPort(couchDbProperties.getPort())
			.setUsername(couchDbProperties.getUsername())
			.setPassword(couchDbProperties.getPassword())
			.setMaxConnections(30000)
			.setConnectionTimeout(30000); // timeout de 30 segundos
	
		couchDbClient = new CouchDbClient(properties);
		return couchDbClient;

	}
}