package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-test.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional
public class ProdutoEdicaoRepositoryImplTest {
	
	@Autowired
	private SessionFactory sf;
	
	@Autowired
	private ProdutoEdicaoRepositoryImpl produtoEdicaoRepository;

	@Before
	public void setUp() {
		
		
		TipoProduto tipoProduto = Fixture.tipoProduto("Revista", GrupoProduto.REVISTA, "99000642");
		getSession().save(tipoProduto);
		
		Produto produto = Fixture.produto("1", "Revista Veja", "Veja", PeriodicidadeProduto.SEMANAL, tipoProduto);
		getSession().save(produto);

		ProdutoEdicao produtoEdicao =
				Fixture.produtoEdicao(1L, 10, 14, new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), produto);
		getSession().save(produtoEdicao);
		
	}
	
	@Test
	public void obterProdutoEdicaoPorNomeProduto() {
		List<ProdutoEdicao> listaProdutoEdicao = 
			produtoEdicaoRepository.obterProdutoEdicaoPorNomeProduto("Veja");
		
		Assert.assertTrue(!listaProdutoEdicao.isEmpty());
	}

	protected void flushClear() {
		getSession().flush();
		getSession().clear();
	}
	
	private Session getSession() {
		return sf.getCurrentSession();
	}

	@Test
	public void obterListaProdutoEdicao() {
		
		Produto produto = new Produto();
		produto.setId(1L);
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setNumeroEdicao(1L);
		
		
		@SuppressWarnings("unused")
		List<ProdutoEdicao> listaProdutoEdicao = 
				produtoEdicaoRepository.obterListaProdutoEdicao(produto, produtoEdicao);
		
	}
	
}
