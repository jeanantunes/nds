package br.com.abril.nds.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import br.com.abril.nds.dto.DetalhesPickingPorCotaModelo03DTO;
import br.com.abril.nds.dto.PickingLEDFullDTO;
import br.com.abril.nds.dto.RetornoPickingDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.LedModelo4IntegracaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Service
public class LedModelo4IntegracaoServiceImpl implements LedModelo4IntegracaoService{
	
	@Autowired
	private CouchDbProperties couchDbProperties;
	
	private CouchDbClient couchDbClient;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	public CouchDbClient getCouchDB_Client(){
		String db_name = "picking_led";
		
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
	
	@Override
	@Transactional
	public void processarRetornoPicking(Date date){
		
		this.couchDbClient = getCouchDB_Client();

		List<PickingLEDFullDTO> registros = new ArrayList<PickingLEDFullDTO>();
		
		String dataFormatada = new SimpleDateFormat("ddMMyyyy").format(date);
		String docName = "pickingLed_" + dataFormatada;
		JsonObject jsonDoc = new JsonObject();
		
		try {
			jsonDoc = couchDbClient.find(JsonObject.class, docName);
		} catch (NoDocumentException e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao processar retorno picking. Documento não encontrado! ");
		}

		if (jsonDoc != null) {
			Gson gson = new Gson();
			JsonArray jaCotas = jsonDoc.getAsJsonArray(dataFormatada);
			
			for (JsonElement jsonElement : jaCotas) {
				PickingLEDFullDTO registroArquivo = gson.fromJson(jsonElement,PickingLEDFullDTO.class);
				registros.add(registroArquivo);
			}
			
		}
		
		List<RetornoPickingDTO> listLancamentos = new ArrayList<>();
		
		for (PickingLEDFullDTO pickingLEDFullDTO : registros) {
			for (DetalhesPickingPorCotaModelo03DTO retornoPickingDTO : pickingLEDFullDTO.getListTrailer2()) {
				if(!isContemLancamento(listLancamentos, retornoPickingDTO.getProduto(), retornoPickingDTO.getEdicao(),pickingLEDFullDTO.getDataLancamento())){
					
					RetornoPickingDTO retornoPicking = new RetornoPickingDTO();
					
					retornoPicking.setCodigoProduto(retornoPickingDTO.getProduto());
					retornoPicking.setNumeroEdicao(retornoPickingDTO.getEdicao());
					retornoPicking.setDataLancamento(pickingLEDFullDTO.getDataLancamento());
					retornoPicking.setDataLed(retornoPickingDTO.getDataLed());
					retornoPicking.setHoraLed(retornoPickingDTO.getHoraLed());
					
					if(!retornoPickingDTO.getProduto().isEmpty() &&
					   !retornoPickingDTO.getEdicao().isEmpty() && 
					   !pickingLEDFullDTO.getDataLancamento().isEmpty()){
						
						Long numEdicao = Long.parseLong(retornoPickingDTO.getEdicao().replace(";", ""));
						Date dataLanc = date;
						String codProduto = ""+Integer.parseInt(retornoPickingDTO.getProduto().replace(";", ""));
						
						retornoPicking.setIdLancamentos(lancamentoRepository.
								buscarIdLancamentoPorDataLancamentoCodProdutoNumEdicaoDataLancamento(codProduto, numEdicao, dataLanc));
					}
					
					listLancamentos.add(retornoPicking);
				}
			}
		}
		
		for (RetornoPickingDTO lancamentoPicking : listLancamentos) {
			lancamentoRepository.atualizarLancamentoSetDadosLED(lancamentoPicking.getIdLancamentos(), 
					lancamentoPicking.getDataLed(), lancamentoPicking.getHoraLed());
		}
		
	}
	
	private boolean isContemLancamento(List<RetornoPickingDTO> listLancamentos, String codigoProduto, String numeroEdicao, String dataLancamento){
		
		
		for (RetornoPickingDTO retornoPickingDTO : listLancamentos) {
			if(retornoPickingDTO.getCodigoProduto().equalsIgnoreCase(codigoProduto)
			   && retornoPickingDTO.getNumeroEdicao().equalsIgnoreCase(numeroEdicao)
			   && retornoPickingDTO.getDataLancamento().equalsIgnoreCase(dataLancamento)){
				return true;
			}
		}
		return false;
	}
	
}
