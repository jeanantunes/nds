package br.com.abril.nds.service.impl;


import org.springframework.context.support.FileSystemXmlApplicationContext;

import br.com.abril.nds.service.EstudoService;

public class EstudoServiceImplTest  {
	
	
	public static void main(String[] args) {
		
		FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext("../nds-client/src/test/resources/applicationContext-local.xml");
				
		EstudoService estudoService = applicationContext.getBean(EstudoService.class);
	
		estudoService.excluirEstudosAnoPassado();
		
	}
}
