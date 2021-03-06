package br.com.dgb.nfe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;

public class ValidarNfeAssinada {

	private static final String NFE = "NFe";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StatusServicoUF.class); 
	
	@Test
	public void validarAssinatura() {

		LOGGER.debug("metodo de validação do arquivo de nfe assinado");
		
		XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM", new org.jcp.xml.dsig.internal.dom.XMLDSigRI());

		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

		String xml = "C:\\opt\\parametros_nds\\notas\\exportado\\NF-e-21-00000021.xml";

		documentFactory.setNamespaceAware(true);

		try {
			DocumentBuilder builder = documentFactory.newDocumentBuilder();

			Document doc = builder.parse(new FileInputStream(xml));

			// Find Signature element.
			NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
			
			if (nl.getLength() == 0) {
				throw new ValidacaoException(TipoMensagem.ERROR, "Cannot find Signature element");
			}
			
			DOMValidateContext valContext = new DOMValidateContext(new KeyValueKeySelector(), nl.item(0));

			XMLSignature signature = null;
			
			signature = factory.unmarshalXMLSignature(valContext);
			
			
			LOGGER.debug("Validar: " + signature.validate(valContext));
			
			LOGGER.debug("Pegar Contexto: " + signature.getSignatureValue().validate(valContext));
			
		    // Valida a XMLSignature.
		    boolean coreValidity = signature.validate(valContext);

		    // Checa o status da validação
		    if (coreValidity == false) {
		    	LOGGER.debug("Falha na Assinatura!");
		    } else {
		    	LOGGER.debug("Assinatura Correta!");
		    }
		    boolean sv = signature.getSignatureValue().validate(valContext);
		    System.out.println("signature validation status: " + sv);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MarshalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLSignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}