package br.com.abril.nds.service.impl;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.service.integracao.ParametroSistemaService;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager="transactionManager")
@ContextConfiguration(locations="file:src/test/resources/applicationContext-test.xml")
public class FecharDiaServiceImplTest {

	@Autowired
	private ParametroSistemaService parametroSistemaService;
	
	@Test
	public void listarDatasValidasLancamento() {
		
		ParametroSistema ps = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO);
		
		String dir = ps.getValor() + File.separator +"reparte"+ File.separator +"zip"+ File.separator;
		File diretorio = new File(dir); 
		for(File input : diretorio.listFiles()) {
			if(input.isDirectory()) continue;			
			input.delete();
		}
		
		dir = ps.getValor() + File.separator +"encalhe"+ File.separator +"zip"+ File.separator;
		diretorio = new File(dir); 
		for(File input : diretorio.listFiles()) {
			if(input.isDirectory()) continue;			
			input.delete();
		}
	}
	
}