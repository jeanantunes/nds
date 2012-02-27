package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoProduto;

public class FornecedorRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private FornecedorRepositoryImpl fornecedorRepository;

	private Fornecedor fornecedor1;
	private Fornecedor fornecedor2;
	private Fornecedor fornecedor3;

	@Before
	public void setUp() {
		PessoaJuridica pj1 = Fixture.pessoaJuridica("FC", "01.001.001/001-00",
				"000.000.000.00", "fc@mail.com");
		PessoaJuridica pj2 = Fixture.pessoaJuridica("Dinap",
				"02.002.002/001-00", "000.000.000.00", "dinap@mail.com");
		PessoaJuridica pj3 = Fixture.pessoaJuridica("Acme",
				"03.003.003/001-00", "000.000.000.00", "acme@mail.com");
		getSession().save(pj1);
		getSession().save(pj2);
		getSession().save(pj3);

		fornecedor1 = Fixture.fornecedor(pj1, SituacaoCadastro.ATIVO, true);
		fornecedor2 = Fixture.fornecedor(pj2, SituacaoCadastro.ATIVO, false);
		fornecedor3 = Fixture.fornecedor(pj3, SituacaoCadastro.PENDENTE, true);
		getSession().save(fornecedor1);
		getSession().save(fornecedor2);
		getSession().save(fornecedor3);
		
		TipoProduto tipoProduto = Fixture.tipoProduto("tipo0", GrupoProduto.REVISTA, "sdfs");
		getSession().save(tipoProduto);
		
		Produto produto = Fixture.produtoCapricho(tipoProduto);
		produto.setFornecedores(new LinkedHashSet<Fornecedor>());
		produto.getFornecedores().add(fornecedor1);
		produto.getFornecedores().add(fornecedor2);
		getSession().save(produto);
		
	}

	@Test
	public void obterFornecedoresDeProduto() {
		
		List<Fornecedor> fornecedores = fornecedorRepository.obterFornecedoresDeProduto("4");
		
		Assert.assertTrue(fornecedores.size() == 2);
		
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
