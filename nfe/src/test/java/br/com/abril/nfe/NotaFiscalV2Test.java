package br.com.abril.nfe;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Test;
import org.xml.sax.SAXException;

public class NotaFiscalV2Test {
	
	private String versaoNFE2 = "2.00";
	
	@Test
	public void validateXMLRetornoVersaoLayout2() throws Exception {

		try {
			String schemaFile = "src/main/resources/xsd/v"+ versaoNFE2 +"/retCancNFe_v"+ versaoNFE2 +".xsd";
			//String xmlFile = "src/main/resources/xmlGerado.xml";
			//String xmlFile = "src/main/resources/nfe-v"+ versaoNFE2 +"-2.xml";
			String xmlFile = "C://opt/parametros_nds/notas/importacao/retCancNFe.xml";
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			System.out.println("Schema: "+ schemaFile);
			Schema schema = factory.newSchema(new File(schemaFile));
			Validator validator = schema.newValidator();
			System.out.println("Validando: "+ xmlFile);
			validator.validate(new StreamSource(new File(xmlFile)));
		} catch (IOException | SAXException e) {
			System.out.println("Exception: "+e.getMessage());
		}

	}

}

