package br.com.abril.xrequers.integration.repository.tests.uai;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.xrequers.integration.repository.tests.ProdutoRepositoryTest;

public class TestUAIProduto extends ProdutoRepositoryTest {
	
	@Test
	public void testInsertProduto() {
		Produto produto = new Produto();
		
		TipoProduto tipoProduto = tipoProdutoRepository.buscarTodos().get(0);
		Long codigo = new Date().getTime();
		produto.setCodigo(codigo.toString());
		produto.setNome("Joquinha");
		produto.setOrigem(Origem.MANUAL);
		produto.setPacotePadrao(123);
		produto.setPeb(123);
		produto.setPeriodicidade(PeriodicidadeProduto.ANUAL);
		produto.setPeso(23L);
		produto.setTipoProduto(tipoProduto);
		
		Long id = produtoRepository.adicionar(produto);
		
		produto = this.produtoRepository.buscarPorId(id);
		
		Assert.assertNotNull(produto);
	}
}
