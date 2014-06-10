package br.com.abril.xrequers.integration.repository.tests.querycompile;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.HistoricoDescontoProdutoRepository;
import br.com.abril.xrequers.integration.repository.tests.AbstractRepositoryTest;

public class QCHistoricoDescontoProdutoRepositoryTest extends AbstractRepositoryTest {

	
	@Autowired
	private HistoricoDescontoProdutoRepository historicoDescontoProdutoRepository;
	
	@Test
	public void test_buscar_historico_por_desconto_e_produto(){
		
		Long idDesconto = null;
		Long idProduto = null;
		
		historicoDescontoProdutoRepository.buscarHistoricoPorDescontoEProduto(idDesconto, idProduto);
		
	}
	
	
}
