package br.com.abril.nds.service.impl;


import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import br.com.abril.nds.service.MatrizDistribuicaoService;

public class MatrizDistribuicaoServiceImplTest  {
	
	
	public static void main(String[] args) {
	
		
		
		FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext("../nds-client/src/main/resources/applicationContext-local.xml");
				
		MatrizDistribuicaoService matrizDistribuicaoService = applicationContext.getBean(MatrizDistribuicaoService.class);
	
		matrizDistribuicaoService.duplicarLinhas(1l);
		
	}
}
