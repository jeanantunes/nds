package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaProdutoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.repository.ProdutoRepository;

public class ProdutoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Before
	public void setUp() {
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		NCM ncmRevistas = Fixture.ncm(99000642l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProduto =
			Fixture.tipoProduto("Revista", GrupoProduto.REVISTA, ncmRevistas, null, 12L);
		save(tipoProduto);
		
		Produto produto =
			Fixture.produto("1", "Revista Veja", "Veja", PeriodicidadeProduto.SEMANAL, tipoProduto, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		produto.setEditor(abril);
		save(produto);
	}
	
	@Test
	public void obterProdutoPorCodigo() {
		Produto produto = 
				produtoRepository.obterProdutoPorCodigo("1");
		
		Assert.assertTrue(produto != null);
	}
	
	@Test
	public void obterProdutoPorNomeProduto() {
		Produto produto = 
			produtoRepository.obterProdutoPorNomeComercial("Veja");
		
		Assert.assertTrue(produto != null);
	}
	
	@Test
	public void obterProdutoLikeNomeProduto() {
		List<Produto> listaProduto = 
			produtoRepository.obterProdutoLikeNomeComercial("Vej");
		
		Assert.assertTrue(!listaProduto.isEmpty());
	}
	
	@Test
	public void pesquisarProdutos() {
		
		List<ConsultaProdutoDTO> listaProdutos = 
			this.produtoRepository.pesquisarProdutos(
				"1", "", "teste", "editor", 1L, "asc", "codigo", 1, 15);
		
		Assert.assertNotNull(listaProdutos);
	}
	
}
