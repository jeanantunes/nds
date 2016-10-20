package br.com.abril.nfe;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.inf.portalfiscal.nfe.v310.TNFe;
import br.inf.portalfiscal.nfe.v310.TNFe.InfNFe.Det;
import br.inf.portalfiscal.nfe.v310.TNfeProc;
import br.inf.portalfiscal.nfe.v310.TProcEvento;

public class LerNfeProc {

	private static Logger LOGGER = LoggerFactory.getLogger(LerNfeProc.class); 
	
	private static String versaoNFE = "3.10";
	private static String PATH_IMPORTACAO = "/opt/parametros_nds/nfe-terceiros/";
	private static final String XSD_PROC_CANC_NFE = "/procEventoCancNFe_v";

	@Test
	public void lerArquivoRetorno() {  
		
		File notaFile = new File(PATH_IMPORTACAO +"NF-e-345-3-00000021-lucro-real.xml");  
		JAXBContext context;	
        Unmarshaller unmarshaller;
			
		try {
				
			context = JAXBContext.newInstance(TNFe.class);  
			unmarshaller = context.createUnmarshaller();  
			TNFe nfeProc = unmarshaller.unmarshal(new StreamSource(notaFile), TNFe.class).getValue();  
				//TNfeProc nfeProc = unmarshaller.unmarshal(new StreamSource(new StringReader(arquivo.getPath())), TNfeProc.class).getValue();
			
			LOGGER.debug("Informaçoes nota fiscal " + nfeProc.getInfNFe().getDet());
			
			
			for(Det detalheProduto : nfeProc.getInfNFe().getDet()) {
				
				LOGGER.debug("iten 1 - " + detalheProduto.getNItem());
				LOGGER.debug("produto - " + detalheProduto.getProd().getCProd());
				
			}
			
			
		} catch (Exception e){
			LOGGER.debug("Erro ao realizar o parser do arquivo de retorno: " + notaFile.getName());
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao realizar o parser do arquivo de retorno:");
		}

	}

	public void lerArquivoEventoCancelamento() {
		
		JAXBContext context;
        Unmarshaller unmarshaller;
        String schemaPath = this.getClass().getClassLoader().getResource("").getPath()+ "../../src/main/resources/";
        
		try {              
			File notaFile = new File(PATH_IMPORTACAO +"110111-35150411411415000174550210000000021000000027-1-procEventoNfe.xml");  
			if(validarSchemaXML(XSD_PROC_CANC_NFE, notaFile, schemaPath, "1.00")) {
	        	
	        	context = JAXBContext.newInstance(TProcEvento.class);
                unmarshaller = context.createUnmarshaller();
                TProcEvento retornoCancelamentoNFe = unmarshaller.unmarshal(new StreamSource(notaFile), TProcEvento.class).getValue();
               
	        } else {
	        	 throw new ValidacaoException(TipoMensagem.ERROR, "Erro com a geração do arquivo ");
	        }
			
			
			context = JAXBContext.newInstance(TProcEvento.class);  
			unmarshaller = context.createUnmarshaller();  
			TProcEvento nfeProcEvento = unmarshaller.unmarshal(new StreamSource(notaFile), TProcEvento.class).getValue();  

			LOGGER.info("Número da chave de acesso: {}", nfeProcEvento.getRetEvento().getInfEvento().getChNFe());  

		} catch (Exception e) {  
			e.printStackTrace();
			LOGGER.info("Erro ao recurperar o arquivo: ", PATH_IMPORTACAO);
		} 
	}
	
	private boolean validarSchemaXML(final String tipoSchema, final File arquivo, final String schemaPath, String versao) {
		
		boolean retorno = false; 
		
		try {
			
			final String schemaFile = schemaPath+"xsd/v"+ (versao != null ? versao : versaoNFE) + tipoSchema + (versao != null ? versao : versaoNFE) + ".xsd";
			final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Schema: "+ schemaFile);
			}
			final Schema schema = factory.newSchema(new File(schemaFile));
			final Validator validator = schema.newValidator();
			validator.validate(new StreamSource(arquivo));
			
			return true;
			
		} catch (IOException | SAXException e) {
			e.printStackTrace();
			LOGGER.error("Exception: ", e);
		}
		
		return retorno;
	}
	
}