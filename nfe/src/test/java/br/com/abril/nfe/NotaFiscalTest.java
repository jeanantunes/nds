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

public class NotaFiscalTest {

	private String versaoNFE2 = "2.00";
	private String versaoNFE = "3.10";
	
	//@Test
	public void validateXML() throws Exception {
		
		try {
            SchemaFactory factory = 
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            //Schema schema = factory.newSchema(new File("src/main/resources/xsd/v3.10/nfe_v3.10.xsd"));
            Schema schema = factory.newSchema(new File("src/main/resources/xsd/v"+ versaoNFE +"/nfe_v"+ versaoNFE +".xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File("src/main/resources/nfe-v"+ versaoNFE +".xml")));
            //validator.validate(new StreamSource(new File("src/main/resources/xmlGerado.xml")));
        } catch (IOException | SAXException e) {
            System.out.println("Exception: "+e.getMessage());
        }

	}
	
	@Test
	public void validateXML2() throws Exception {
		
		try {
            SchemaFactory factory = 
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            //Schema schema = factory.newSchema(new File("src/main/resources/xsd/v3.10/nfe_v3.10.xsd"));
            Schema schema = factory.newSchema(new File("src/main/resources/xsd/v"+ versaoNFE2 +"/nfe_v"+ versaoNFE2 +".xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File("src/main/resources/nfe-v"+ versaoNFE2 +".xml")));
        } catch (IOException | SAXException e) {
            System.out.println("Exception: "+e.getMessage());
        }

	}
}
