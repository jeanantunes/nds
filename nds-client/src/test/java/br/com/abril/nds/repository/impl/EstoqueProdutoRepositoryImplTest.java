package br.com.abril.nds.repository.impl;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.estoque.EstoqueProduto;

public class EstoqueProdutoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private EstoqueProdutoRepositoryImpl estoqueProdutoRepositoryImpl;
	
	@Test
	public void testarBuscarEstoquePorProduto() {
		
		EstoqueProduto estoqueProduto;
		
		Long idProdutoEdicao = 1L;
		
		estoqueProduto = estoqueProdutoRepositoryImpl.buscarEstoquePorProduto(idProdutoEdicao);
		
//		Assert.assertNull(estoqueProduto);
		
	}
	
	@Test
	public void testarBuscarEstoqueProdutoPorProdutoEdicao() {
		
		EstoqueProduto estoqueProduto;
		
		Long idProdutoEdicao = 1L;
		
		estoqueProduto = estoqueProdutoRepositoryImpl.buscarEstoqueProdutoPorProdutoEdicao(idProdutoEdicao);
		
//		Assert.assertNull(estoqueProduto);
		
	}

}
