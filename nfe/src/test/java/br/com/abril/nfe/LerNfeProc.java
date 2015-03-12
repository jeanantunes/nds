package br.com.abril.nfe;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import br.inf.portalfiscal.nfe.v310.TNfeProc;

public class LerNfeProc {

	 public static void main(String[] args) {  
		  
		 try {              
			 File notaFile = new File("C:/opt/parametros_nds/notas/importado/-procNfe.xml");  
			 JAXBContext context = JAXBContext.newInstance(TNfeProc.class);  
		   
			 Unmarshaller unmarshaller = context.createUnmarshaller();  
		   
			 TNfeProc nfeProc = unmarshaller.unmarshal(new StreamSource(notaFile), TNfeProc.class).getValue();  
		   
			 System.out.println(nfeProc.getProtNFe().getInfProt().getChNFe());  
		   
		 } catch (Exception e) {  
			 e.printStackTrace();  
		   
		 }  
	}
}
