package br.com.abril.nds.strategy.importacao;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.ImportacaoArquivoService;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import br.com.abril.nds.util.TipoImportacaoArquivo;

public class ImportacaoDeArquivoProdutoStrategyTest extends AbstractRepositoryImplTest{
	

	private RetornoImportacaoArquivoVO retorno;
	private File file;
	private final String PATHFILE = "src/test/resources/importacao/PRODUTO.NEW";
	
	@Autowired
	private ImportacaoArquivoService importacaoDeArquivoProdutoStrategy; 
	
	@Before
	public void setUp() throws Exception {
		retorno = new RetornoImportacaoArquivoVO();
		file = new File(PATHFILE);	
	}

	
	@Test
	public void testProcessarImportacaoArquivo() throws IOException  {
		Assert.assertTrue(file!=null && file.length() > 0);
		try{
			retorno = importacaoDeArquivoProdutoStrategy.processarImportacaoArquivo(file,TipoImportacaoArquivo.PRODUTO);
	    	Assert.assertEquals(true, retorno!=null && retorno.isSucessoNaImportacao());
	    }
		catch(Exception e){
			Assert.assertTrue(false);
		}
	}

}
