package br.com.abril.nds.repository.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;

public class FornecedorRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private FornecedorRepositoryImpl fornecedorRepository;

	private Fornecedor fornecedor1;
	private Fornecedor fornecedor2;
	private Fornecedor fornecedor3;

	private Produto produto;

	@Before
	public void setUp() {
		TipoFornecedor fornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		TipoFornecedor fornecedorOutros = Fixture.tipoFornecedorOutros();
		save(fornecedorPublicacao, fornecedorOutros);
		
		fornecedor1 = Fixture.fornecedorFC(fornecedorPublicacao);
		fornecedor2 = Fixture.fornecedorAcme(fornecedorOutros);
		fornecedor3 = Fixture.fornecedorDinap(fornecedorPublicacao);
		fornecedor3.setSituacaoCadastro(SituacaoCadastro.PENDENTE);
		save(fornecedor1, fornecedor2, fornecedor3);
		
		TipoProduto tipoProduto = Fixture.tipoRevista();
		save(tipoProduto);
		
		produto = Fixture.produtoCapricho(tipoProduto);
		produto.addFornecedor(fornecedor1);
		produto.addFornecedor(fornecedor3);
		save(produto);
		
	}

	@Test
	public void obterFornecedoresDeProduto() {
		List<Fornecedor> fornecedores = fornecedorRepository.obterFornecedoresDeProduto(produto.getCodigo());
		Assert.assertTrue(fornecedores.size() == 1);
		Assert.assertTrue(fornecedores.contains(fornecedor1));
	}
	
	@Test
	public void obterFornecedoresPermitemBalanceamento() {
		List<Fornecedor> fornecedores = fornecedorRepository.obterFornecedores(
				true, SituacaoCadastro.ATIVO);
		Assert.assertEquals(1, fornecedores.size());
		Assert.assertTrue(fornecedores.contains(fornecedor1));
	}
	
	@Test
	public void obterFornecedoresNaoPermitemBalanceamento() {
		List<Fornecedor> fornecedores = fornecedorRepository.obterFornecedores(
				false, SituacaoCadastro.ATIVO);
		Assert.assertEquals(1, fornecedores.size());
		Assert.assertTrue(fornecedores.contains(fornecedor2));
	}

}
