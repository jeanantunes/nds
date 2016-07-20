package br.com.abril.ndsled.actions;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import org.lightcouch.NoDocumentException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.abril.ndsled.exceptions.CarregarLancamentoException;
import br.com.abril.ndsled.modelo.DetalhesPickingPorCotaModelo04DTO;
import br.com.abril.ndsled.modelo.Lancamento;
import br.com.abril.ndsled.modelo.PickingLEDFullDTO;

public class AppActions {
	
	private static final String DB_NAME = "picking_led";
	
	private CouchDbClient couchDbClient;
	
	private CouchDbProperties properties = new CouchDbProperties()
			  .setDbName(DB_NAME)
			  .setCreateDbIfNotExist(true)
			  .setProtocol("https")
			  .setHost("localhost")
			  .setPort(5984)
			  .setUsername("admin")
			  .setPassword("admin")
			  .setMaxConnections(100)
			  .setConnectionTimeout(500);

	//TODO main TEST - EXCLUIR APÓS TESTES
	public static void main(String[] args) {
		
		CouchDbProperties properties = new CouchDbProperties()
				  .setDbName(DB_NAME)
				  .setCreateDbIfNotExist(true)
				  .setProtocol("http")
				  .setHost("localhost")
				  .setPort(5984)
				  .setUsername("admin")
				  .setPassword("admin")
				  .setMaxConnections(100)
				  .setConnectionTimeout(5000);
		
		CouchDbClient couchDbClient = new CouchDbClient(properties);
		
		List<PickingLEDFullDTO> registros = new ArrayList<PickingLEDFullDTO>();
		
		String dataFormatada = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()); 

		String docName = "pickingLed_"+dataFormatada;
		
		JsonObject jsonDoc = new JsonObject();
		
		try {
			jsonDoc = couchDbClient.find(JsonObject.class, docName);
		} catch (NoDocumentException e) {

		}
		
		if(jsonDoc != null){
			
			Gson gson = new Gson();

			JsonArray jaCotas = jsonDoc.getAsJsonArray(dataFormatada);
			
			for (JsonElement jsonElement : jaCotas) {
				PickingLEDFullDTO registroArquivo = gson.fromJson(jsonElement, PickingLEDFullDTO.class);
				registros.add(registroArquivo);
			}
		
		}
		
		for (PickingLEDFullDTO det : registros) {
			System.out.println(det.toString());
			System.out.println(det.getEnderecoLED());
			System.out.println(det.getObservacoes02());
		}
		
	}
	//TODO FIM
	
	public List<Lancamento> carregarLancamento(Date date) throws CarregarLancamentoException {
		// Aqui vai ter uma chamada para o metodo que vai fazer a comunicacao
		// com o couch.

		// dados importados via couch
		List<DetalhesPickingPorCotaModelo04DTO> listDadosImportadosViaCouchDb = importarPickingLED();
		
		List<Lancamento> lancamentos = new ArrayList<Lancamento>();

		Lancamento lancamento = new Lancamento();
		lancamento.setCodigoCota(1);
		lancamento.setCodigoProduto(552);
		lancamento.setDataLacamento(new java.sql.Date(2016, 04, 01));
		lancamento.setDesconto(new BigDecimal(0));
		lancamento.setEdicaoProduto(1);
		lancamento.setNomeProduto("Veja");
		lancamento.setPrecoCapa(new BigDecimal(14.99));
		lancamento.setPrecoCusto(new BigDecimal(14.99));
		lancamento.setQuantidadeReparte(10);
		lancamento.setCodigoLed(2);
		lancamentos.add(lancamento);

		lancamento = new Lancamento();
		lancamento.setCodigoCota(2);
		lancamento.setCodigoProduto(552);
		lancamento.setDataLacamento(new java.sql.Date(2016, 04, 01));
		lancamento.setDesconto(new BigDecimal(0));
		lancamento.setEdicaoProduto(1);
		lancamento.setNomeProduto("Veja");
		lancamento.setPrecoCapa(new BigDecimal(14.99));
		lancamento.setPrecoCusto(new BigDecimal(14.99));
		lancamento.setQuantidadeReparte(7);
		lancamento.setCodigoLed(49);
		lancamentos.add(lancamento);

		lancamento = new Lancamento();
		lancamento.setCodigoCota(1);
		lancamento.setCodigoProduto(111222);
		lancamento.setDataLacamento(new java.sql.Date(2016, 04, 01));
		lancamento.setDesconto(new BigDecimal(0));
		lancamento.setEdicaoProduto(1);
		lancamento.setNomeProduto("Avengers");
		lancamento.setPrecoCapa(new BigDecimal(14.99));
		lancamento.setPrecoCusto(new BigDecimal(14.99));
		lancamento.setQuantidadeReparte(25);
		lancamento.setCodigoLed(2);
		lancamentos.add(lancamento);

		lancamento = new Lancamento();
		lancamento.setCodigoCota(2);
		lancamento.setCodigoProduto(111222);
		lancamento.setDataLacamento(new java.sql.Date(2016, 04, 01));
		lancamento.setDesconto(new BigDecimal(0));
		lancamento.setEdicaoProduto(1);
		lancamento.setNomeProduto("Avengers");
		lancamento.setPrecoCapa(new BigDecimal(4.99));
		lancamento.setPrecoCusto(new BigDecimal(4.99));
		lancamento.setQuantidadeReparte(50);
		lancamento.setCodigoLed(49);
		lancamentos.add(lancamento);

		return lancamentos;

	}
	
	public List<DetalhesPickingPorCotaModelo04DTO> importarPickingLED(){
		
		this.couchDbClient = new CouchDbClient(properties);
		
		List<DetalhesPickingPorCotaModelo04DTO> registros = new ArrayList<DetalhesPickingPorCotaModelo04DTO>();
		
		String dataFormatada = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()); 

		String docName = "pickingLed_"+dataFormatada;
		
		JsonObject jsonDoc = new JsonObject();
		
		try {
			jsonDoc = couchDbClient.find(JsonObject.class, docName);
		} catch (NoDocumentException e) {

		}
		
		if(jsonDoc != null){
			
			Gson gson = new Gson();

			JsonArray jaCotas = jsonDoc.getAsJsonArray(dataFormatada);
			
			for (JsonElement jsonElement : jaCotas) {
				DetalhesPickingPorCotaModelo04DTO registroArquivo = gson.fromJson(jsonElement, DetalhesPickingPorCotaModelo04DTO.class);
				registros.add(registroArquivo);
			}
		
		}
		return registros;
	}

}
