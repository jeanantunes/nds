package br.com.abril.xrequers.integration.repository.tests.pe;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.xrequers.integration.repository.tests.ProdutoRepositoryTest;

public class TestPEProduto extends ProdutoRepositoryTest {
	
	
	@Test
	public void testPEProduto() {
		List<Produto> produtos = produtoRepository.buscarTodos();
		Assert.assertNotNull(produtos);
	}
	
}
