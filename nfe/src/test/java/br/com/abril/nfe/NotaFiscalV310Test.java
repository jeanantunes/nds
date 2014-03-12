package br.com.abril.nfe;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class NotaFiscalV310Test {
	
	private static Logger LOGGER = LoggerFactory.getLogger(NotaFiscalV310Test.class);

	private static String xmlFile = "/home/sergio/Dropbox/DGB/NDS/Modelagem/NF-e/Certificados/nfeassinada.xml";
	private String versaoNFE = "3.10";
	
	@Test
	public void validateXML() throws Exception {

		try {
			String schemaFile = "src/main/resources/xsd/v"+ versaoNFE +"/nfe_v"+ versaoNFE +".xsd";
			
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			LOGGER.debug("Schema: "+ schemaFile);
			Schema schema = factory.newSchema(new File(schemaFile));
			Validator validator = schema.newValidator();
			LOGGER.debug("Validando: "+ xmlFile);
			validator.validate(new StreamSource(new File(xmlFile)));
		} catch (IOException | SAXException e) {
			LOGGER.error("Exception: "+ e.getMessage());
		}

	}

	@Test
	public void validateXMLRetorno() throws Exception {

		try {
			String schemaFile = "src/main/resources/xsd/v"+ versaoNFE +"/cancNFe_v"+ versaoNFE +".xsd";
			//String xmlFile = "src/main/resources/xmlGerado.xml";
			String xmlFile = "C://opt/parametros_nds/notas/importacao/retCancNFe.xml";
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			LOGGER.debug("Schema: "+ schemaFile);
			Schema schema = factory.newSchema(new File(schemaFile));
			Validator validator = schema.newValidator();
			LOGGER.debug("Validando: "+ xmlFile);
			validator.validate(new StreamSource(new File(xmlFile)));
		} catch (IOException | SAXException e) {
			LOGGER.error("Exception: "+ e.getMessage());
		}

	}
	
}