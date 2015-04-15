package br.com.abril.nds.ftf;

import java.io.IOException;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import br.com.abril.nds.service.PessoaCRPWSService;

public class IntegracaoCRPWebServiceTest  {
	
	public static void main(String[] args) throws IOException {
	
		FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext("//home/samuel/infoa2/workspace/NDS/nds-client/src/main/resources/applicationContext-local.xml");
				
		PessoaCRPWSService ftfService = applicationContext.getBean(PessoaCRPWSService.class);
		
		//"NDS", 1, "68252618000182"
		//"NDS", 2, "04113953864"
		ftfService.obterDadosFiscais(2, "04113953864");
		
	}
}
