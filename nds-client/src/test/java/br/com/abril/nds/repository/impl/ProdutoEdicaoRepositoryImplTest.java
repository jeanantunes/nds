package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;

public class ProdutoEdicaoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ProdutoEdicaoRepositoryImpl produtoEdicaoRepository;

	@Before
	public void setUp() {
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		Fornecedor dinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(dinap);
		
		TipoProduto tipoProduto = Fixture.tipoRevista();
		save(tipoProduto);
		
		Produto produto = Fixture.produtoVeja(tipoProduto);
		produto.addFornecedor(dinap);
		produto.setEditor(abril);
		save(produto);

		ProdutoEdicao produtoEdicao =
				Fixture.produtoEdicao(1L, 10, 14, new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), produto);
		save(produtoEdicao);
		
		Lancamento lancamento = 
				Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao, 
						new Date(), new Date(), new Date(), new Date(), BigDecimal.TEN, StatusLancamento.CONFIRMADO, null, 1);
		save(lancamento);
		
		Estudo estudo = 
				Fixture.estudo(BigDecimal.TEN, new Date(), produtoEdicao);
		save(estudo);
	}
	
	@Test
	public void obterProdutoEdicaoPorNomeProduto() {
		List<ProdutoEdicao> listaProdutoEdicao = 
			produtoEdicaoRepository.obterProdutoEdicaoPorNomeProduto("Veja");
		
		Assert.assertTrue(!listaProdutoEdicao.isEmpty());
	}
	
	@Test
	public void obterProdutoEdicaoPorCodProdutoNumEdicao() {
		ProdutoEdicao produtoEdicao = 
			produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao("1", 1L);
		
		Assert.assertTrue(produtoEdicao != null);
	}
	
	@Test
	public void obterProdutoEdicaoPorCodigoEdicaoDataLancamento(){
		FuroProdutoDTO furoProdutoDTO = 
				produtoEdicaoRepository.obterProdutoEdicaoPorCodigoEdicaoDataLancamento("1", null, 1L, new Date());
		
		Assert.assertTrue(furoProdutoDTO != null);
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
