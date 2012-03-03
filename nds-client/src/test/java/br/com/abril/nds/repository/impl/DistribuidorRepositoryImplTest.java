package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;

public class DistribuidorRepositoryImplTest extends AbstractRepositoryImplTest {

	
	@Autowired
	private DistribuidorRepositoryImpl distribuidorRepository;

	private Distribuidor distribuidor;
	private Fornecedor fornecedorFC; 
	private Fornecedor fornecedorDinap; 
	private DistribuicaoFornecedor dinapSegunda;
	private DistribuicaoFornecedor dinapQuarta;
	private DistribuicaoFornecedor dinapSexta;
	private DistribuicaoFornecedor fcSegunda;
	private DistribuicaoFornecedor fcSexta;
	
	@Before
	public void setUp() {
		PessoaJuridica pj = Fixture.pessoaJuridica("Distrib", "01.001.001/001-00",
				"000.000.000.00", "distrib@mail.com");
		distribuidor = Fixture.distribuidor(pj, new Date());
		save(pj);
		save(distribuidor);
		
		fornecedorFC = Fixture.fornecedorFC();
		fornecedorDinap = Fixture.fornecedorDinap();
		save(fornecedorFC, fornecedorDinap);
		
		dinapSegunda = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.SEGUNDA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		dinapQuarta = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.QUARTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		dinapSexta = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.SEXTA_FEIRA,
				OperacaoDistribuidor.RECOLHIMENTO);
		save(dinapSegunda, dinapQuarta, dinapSexta);

		fcSegunda = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorFC, DiaSemana.SEGUNDA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		fcSexta = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorFC, DiaSemana.SEXTA_FEIRA,
				OperacaoDistribuidor.RECOLHIMENTO);
		save(fcSegunda, fcSexta);
	}
	
	@Test
	public void obter() {
		Distribuidor distribuidorDB = distribuidorRepository.obter();
		Assert.assertNotNull(distribuidorDB);
		Assert.assertEquals(distribuidor.getId(), distribuidorDB.getId());
	}
	
	@Test
	public void buscarDiasDistribuicao() {
		List<Long> idsFornecedores = new ArrayList<Long>(2);
		idsFornecedores.add(fornecedorFC.getId());
		idsFornecedores.add(fornecedorDinap.getId());
		List<DistribuicaoFornecedor> resultado = distribuidorRepository
				.buscarDiasDistribuicao(idsFornecedores);
		Assert.assertEquals(3, resultado.size());
		Assert.assertTrue(resultado.contains(dinapSegunda));
		Assert.assertTrue(resultado.contains(dinapQuarta));
		Assert.assertTrue(resultado.contains(fcSegunda));
		
	}

}
