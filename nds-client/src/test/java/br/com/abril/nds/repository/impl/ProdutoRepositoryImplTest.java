package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.repository.ProdutoRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-test.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional
public class ProdutoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Before
	public void setUp() {
		TipoProduto tipoProduto =
			Fixture.tipoProduto("Revista", GrupoProduto.REVISTA, "99000642");
		getSession().save(tipoProduto);
		
		Produto produto =
			Fixture.produto("1", "Revista Veja", "Veja", PeriodicidadeProduto.SEMANAL, tipoProduto);
		getSession().save(produto);
	}
	
	@Test
	public void obterProdutoPorCodigo() {
		Produto produto = 
				produtoRepository.obterProdutoPorCodigo("1");
		
		Assert.assertTrue(produto != null);
	}
	
	@Test
	public void obterProdutoPorNomeProduto() {
		List<Produto> listaProduto = 
			produtoRepository.obterProdutoLikeNomeProduto("Veja");
		
		Assert.assertTrue(!listaProduto.isEmpty());
	}
	
}
