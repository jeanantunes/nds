package br.com.abril.nds.strategy.importacao;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.ImportacaoArquivoService;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import br.com.abril.nds.util.TipoImportacaoArquivo;

public class ImportacaoDeArquivoMatrizStrategyTest extends AbstractRepositoryImplTest {

	@Autowired
	private ImportacaoArquivoService importacaoArquivoService;
	
	@Test
	public void testeImportacaoMatrizComErro(){
		
		File arquivo  = new File ("src/test/resources/importacao/MATRIZ.NEW");
		
		RetornoImportacaoArquivoVO retornoImportacaoArquivoVO  = importacaoArquivoService.processarImportacaoArquivo(arquivo, TipoImportacaoArquivo.MATRIZ);
		
		Assert.assertNotNull(retornoImportacaoArquivoVO);
		
		Assert.assertTrue(!retornoImportacaoArquivoVO.isSucessoNaImportacao());
	}
	
}
