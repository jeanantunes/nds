package br.com.abril.nfe;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.inf.portalfiscal.nfe.v310.TNfeProc;

public class LerNfeProc {

	private static Logger LOGGER = LoggerFactory.getLogger(LerNfeProc.class); 
	
	private static String PATH_ARQUIVO = "C:/opt/parametros_nds/notas/importado/-procNfe.xml";
	
	@Test
	public static void lerArquivoRetorno() {  
		  
		try {              
			 File notaFile = new File(PATH_ARQUIVO);  
			 JAXBContext context = JAXBContext.newInstance(TNfeProc.class);  
		   
			 Unmarshaller unmarshaller = context.createUnmarshaller();  
		   
			 TNfeProc nfeProc = unmarshaller.unmarshal(new StreamSource(notaFile), TNfeProc.class).getValue();  
		   
			 LOGGER.info("Número da chave de acesso: ", nfeProc.getProtNFe().getInfProt().getChNFe());  
		   
		 } catch (Exception e) {  
			 LOGGER.info("Erro ao recurperar o arquivo: ", PATH_ARQUIVO);
		 }  
	}
}
