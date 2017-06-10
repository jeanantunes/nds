package br.com.abril.ndsled.actions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import org.lightcouch.NoDocumentException;

import br.com.abril.ndsled.exceptions.CarregarLancamentoException;
import br.com.abril.ndsled.modelo.Cota;
import br.com.abril.ndsled.modelo.DetalhesPickingPorCotaModelo04DTO;
import br.com.abril.ndsled.modelo.Lancamento;
import br.com.abril.ndsled.modelo.PickingLEDFullDTO;
import br.com.abril.ndsled.modelo.Produto;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Classe com Metodos Statics para realizar acoes.
 * 
 * @author Andre W Silva
 * @since 19/07/2016
 * 
 */
public class AppActions {
	// TODO main TEST - EXCLUIR APÓS TESTES
	/*
	 * public static void main(String[] args) {
	 * 
	 * CouchDbProperties properties = new CouchDbProperties()
	 * .setDbName(DB_NAME) .setCreateDbIfNotExist(true) .setProtocol("http")
	 * .setHost("localhost") .setPort(5984) .setUsername("admin")
	 * .setPassword("admin") .setMaxConnections(100)
	 * .setConnectionTimeout(5000);
	 * 
	 * CouchDbClient couchDbClient = new CouchDbClient(properties);
	 * 
	 * List<PickingLEDFullDTO> registros = new ArrayList<PickingLEDFullDTO>();
	 * 
	 * String dataFormatada = new
	 * SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime());
	 * 
	 * String docName = "pickingLed_"+dataFormatada;
	 * 
	 * JsonObject jsonDoc = new JsonObject();
	 * 
	 * try { jsonDoc = couchDbClient.find(JsonObject.class, docName); } catch
	 * (NoDocumentException e) {
	 * 
	 * }
	 * 
	 * if(jsonDoc != null){
	 * 
	 * Gson gson = new Gson();
	 * 
	 * JsonArray jaCotas = jsonDoc.getAsJsonArray(dataFormatada);
	 * 
	 * for (JsonElement jsonElement : jaCotas) { PickingLEDFullDTO
	 * registroArquivo = gson.fromJson(jsonElement, PickingLEDFullDTO.class);
	 * registros.add(registroArquivo); }
	 * 
	 * }
	 * 
	 * for (PickingLEDFullDTO det : registros) {
	 * System.out.println(det.toString());
	 * System.out.println(det.getEnderecoLED());
	 * System.out.println(det.getObservacoes02()); }
	 * 
	 * }
	 */
	// TODO FIM

	/**
	 * Metodo para carregar o Lancamento por data do CouchDB
	 * 
	 * @param date
	 * @return List Lancamento
	 * @throws CarregarLancamentoException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<Lancamento> carregarLancamento(Date date)
			throws CarregarLancamentoException, FileNotFoundException,
			IOException, Exception {

		// Properties props = loadProperties(new FileInputStream(
		// "./couchdb.properties"));

		Properties props = loadProperties(AppActions.class.getClassLoader()
				.getResourceAsStream("app.properties"));

		CouchDbProperties couchDbProperties = new CouchDbProperties()
				.setDbName(props.getProperty("couchdb.dbname"))
				.setCreateDbIfNotExist(true)
				.setProtocol(props.getProperty("couchdb.protocol"))
				.setHost(props.getProperty("couchdb.host"))
				.setPort(Integer.parseInt(props.getProperty("couchdb.port")))
				.setUsername(props.getProperty("couchdb.username"))
				.setPassword(props.getProperty("couchdb.password"))
				.setMaxConnections(100).setConnectionTimeout(5000);

		CouchDbClient couchDbClient = new CouchDbClient(couchDbProperties);

		List<PickingLEDFullDTO> registros = new ArrayList<PickingLEDFullDTO>();
		String dataFormatada = new SimpleDateFormat("ddMMyyyy").format(date);
		String docName = "pickingLed_" + dataFormatada;
		JsonObject jsonDoc = new JsonObject();
		try {
			jsonDoc = couchDbClient.find(JsonObject.class, docName);
		} catch (NoDocumentException e) {
			throw new CarregarLancamentoException(
					"Lançamento não encontrado para essa Data.");
		}

		if (jsonDoc != null) {
			Gson gson = new Gson();
			JsonArray jaCotas = jsonDoc.getAsJsonArray(dataFormatada);
			for (JsonElement jsonElement : jaCotas) {
				PickingLEDFullDTO registroArquivo = gson.fromJson(jsonElement,
						PickingLEDFullDTO.class);
				registros.add(registroArquivo);
			}
		}

		List<Lancamento> lancamentos = new ArrayList<Lancamento>();
		for (PickingLEDFullDTO det : registros) {
			// System.out.println(det.toString());
			// System.out.println("Cota:"+det.getCodigoCotaLinha1());
			// System.out.println("Led:" + det.getEnderecoLED());
			// System.out.println("Box: " + det.getCodigoBox());

			List<DetalhesPickingPorCotaModelo04DTO> det1 = det
					.getListTrailer2();

			for (DetalhesPickingPorCotaModelo04DTO item : det1) {
				Lancamento lancamento = new Lancamento();
				lancamento.setCodigoCota(new Integer(det.getCodigoCotaLinha1()
						.replace(";", "")));
				lancamento.setCodigoProduto(new Integer(item.getProduto()
						.replace(";", "")));
				lancamento.setCodigoBox(new Integer(det.getCodigoBox().replace(
						";", "")));
				lancamento.setDataLacamento(new SimpleDateFormat("ddMMyyyy")
						.parse(det.getDataLancamento().replace(";", "")));
				lancamento.setEdicaoProduto(new Integer(item.getEdicao()
						.replace(";", "")));
				lancamento.setNomeProduto(item.getNome().replace(";", ""));
				lancamento.setQuantidadeReparte(new Integer(item
						.getQuantidade().replace(";", "")));
				lancamento.setCodigoLed(new Integer(det.getEnderecoLED()
						.replace(";", "")));
				lancamento.setCodigoBarras(new Long(item.getCodigoDeBarras()
						.replace(";", "")));
				lancamentos.add(lancamento);
			}

		}

		return lancamentos;

	}

	/**
	 * Metodo Util para carregar uma arquivo de Properties
	 * 
	 * @param in
	 *            Espera uma arquivo .properties
	 * @return Properties
	 * @throws IOException
	 */
	public static Properties loadProperties(InputStream in) throws IOException {
		Properties props = new Properties();
		props.load(in);
		in.close();
		return props;
	}

	/**
	 * Metodo usado para verificar cota sem led.
	 * 
	 * @param cotas
	 * @return boolean
	 */
	public static boolean verificarCotaSemLed(List<Cota> cotas) {
		Iterator<Cota> iCotas = cotas.iterator();
		boolean retorno = false;
		while (iCotas.hasNext()) {
			if (iCotas.next().getCodLed() == 0) {
				retorno = true;
				break;
			}
		}
		iCotas = null;
		return retorno;
	}

	/**
	 * Metodo usado para setar maxlength para um campo textfield.
	 * 
	 * @param str
	 * @return string
	 */
	public static String maxlength(String str, int tamanho) {
		String valor = "";
		if (str.length() > tamanho) {
			valor = str.substring(0, tamanho);
		}
		return valor;
	}

	public static Produto getProdutoByCodBarras(String codigoBarras,
			List<Produto> lstProdutosAgrupados) throws Exception {
		Long codBarras = new Long(codigoBarras);
		
		
		Iterator<Produto> iProduto = lstProdutosAgrupados.iterator();
		while (iProduto.hasNext()) {
			Produto produto = iProduto.next();
			if(produto.getCodigoBarras().compareTo(codBarras) == 0){
				return produto;
			} 
		}

		return null;
	}
}
