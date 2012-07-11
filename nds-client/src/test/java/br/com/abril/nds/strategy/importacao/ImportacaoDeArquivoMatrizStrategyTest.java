package br.com.abril.nds.strategy.importacao;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.ImportacaoArquivoService;

public class ImportacaoDeArquivoMatrizStrategyTest extends AbstractRepositoryImplTest {

	@Autowired
	private ImportacaoArquivoService importacaoArquivoService;
	
	@Ignore
	@Test
	public void testeImportacaoMatriz(){
		
		//File arquivo  = new File ("src/test/resources/importacao/MATRIZ.NEW");
		
		//ImportacaoArquivoStrategy importacaoArquivoStrategy = ImportacaoArquivoFactory.getStrategy(TipoImportacaoArquivo.MATRIZ,fixedFormatManager);
		
		//RetornoImportacaoArquivoVO retornoImportacaoArquivoVO = importacaoArquivoStrategy.processarImportacaoArquivo(arquivo); 
		
		//RetornoImportacaoArquivoVO retornoImportacaoArquivoVO  = importacaoArquivoService.processarImportacaoArquivo(arquivo, TipoImportacaoArquivo.MATRIZ);
		
		//Assert.assertNotNull(retornoImportacaoArquivoVO);
		
		//Assert.assertTrue(retornoImportacaoArquivoVO.isSucessoNaImportacao());
	}
	
}
