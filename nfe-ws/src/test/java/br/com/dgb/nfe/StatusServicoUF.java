package br.com.dgb.nfe;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nfe.integracao.NfeRecepcao2Stub;

public class StatusServicoUF {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatusServicoUF.class); 
	
	// colecao de estados do ambiente virual nacional
	private static Collection<String> SVAN = new ArrayList<String>();
	
	// colecao de estados do ambiente virual do RS
	private static Collection<String> SVRS = new ArrayList<String>();
		
	
	@Test
	public String status() {
		
		String xml = "";
		
		try {
			Document doc = UtilServer.getXml(xml);
			String uf = UtilServer.getValorTag(doc.getDocumentElement(), "cUF", false);
			String ambiente = UtilServer.getValorTag(doc.getDocumentElement(), "tpAmb", false);
			String url = identificaXml(uf, ambiente, "NfeStatusServico", false);
			String versao = doc.getDocumentElement().getAttribute("versao");

			OMElement ome = AXIOMUtil.stringToOM(xml);
			NfeRecepcao2Stub.NfeDadosMsg dadosMsg = new NfeRecepcao2Stub.NfeDadosMsg();
			dadosMsg.setExtraElement(ome);

			NfeRecepcao2Stub.NfeCabecMsg nfeCabecMsg = new NfeRecepcao2Stub.NfeCabecMsg();
			nfeCabecMsg.setCUF(uf);
			nfeCabecMsg.setVersaoDados(versao);

			NfeRecepcao2Stub.NfeCabecMsgE nfeCabecMsgE = new NfeRecepcao2Stub.NfeCabecMsgE();
			nfeCabecMsgE.setNfeCabecMsg(nfeCabecMsg);

			NfeRecepcao2Stub stub = new NfeRecepcao2Stub(url);
			NfeRecepcao2Stub.NfeRecepcaoLote2Result result = stub.nfeRecepcaoLote2(dadosMsg, nfeCabecMsgE);

			return result.getExtraElement().toString();
		} catch (Exception e) {
			LOGGER.error("Erro ao solicitar o servico na sefaz :: ", e);
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração do documento da NE. O Tipo de documento deve ser Nota de Envio.");
		}
	}
	
	@SuppressWarnings("unused")
	private String identificaXml(String uf, String tipo, String servico, boolean scan) throws ValidacaoException {
		try {
			// identifica se o estado é virtual ou scan
			String chave;
			if (scan) {
				chave = "SCAN_" + servico;
			} else if (SVAN.contains(uf)) {
				chave = "SVAN_" + servico;
			} else if (SVRS.contains(uf)) {
				chave = "SVRS_" + servico;
			} else {
				chave = uf + "_" + servico;
			}

			// identifica a url
			String url = "xxxxxxxxxx";
			
			if (url == null) {
				throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração do documento da NE. O Tipo de documento deve ser Nota de Envio");
			} else {
				return url;
			}
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração do documento da NE. O Tipo de documento deve ser Nota de Envio");
		}
	}
}