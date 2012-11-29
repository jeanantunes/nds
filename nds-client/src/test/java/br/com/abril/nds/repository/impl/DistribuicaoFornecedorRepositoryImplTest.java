package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.hibernate.mapping.Set;
import org.junit.Assert;
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
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoFornecedor;

public class DistribuicaoFornecedorRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private DistribuicaoFornecedorRepositoryImpl distribuicaoFornecedorRepository;
	
	private Fornecedor fornecedorAcme;
	private DistribuicaoFornecedor dinapSegunda; 
	private DistribuicaoFornecedor 	dinapQuarta; 
	
	@Before
	public void setUp(){
		
		TipoFornecedor tipoFornecedorOutros = Fixture.tipoFornecedorOutros();
		save(tipoFornecedorOutros);
		
		fornecedorAcme = Fixture.fornecedorAcme(tipoFornecedorOutros);
		save(fornecedorAcme);
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000147", "110042490114", "distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);
		
		Distribuidor distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), new HashSet<PoliticaCobranca>());
		save(distribuidor);
		
		dinapSegunda = Fixture.distribuicaoFornecedor(
				fornecedorAcme, DiaSemana.SEGUNDA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		save(dinapSegunda);
		
		dinapQuarta = Fixture.distribuicaoFornecedor(
				fornecedorAcme, DiaSemana.QUARTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		
		
	}
	
	@Test
	public void testObterDiasSemanaDistribuicao(){
		
		
		List<Integer> lista =  distribuicaoFornecedorRepository.obterDiasSemanaDistribuicao("1", 1L);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void verificarDistribuicaoDiaSemana(){
		
		String codigoProduto  = "1a";
		Long idProdutoEdicao = 2L;
		DiaSemana diaSemana = DiaSemana.QUINTA_FEIRA;  
		
		boolean verificaDistribuicaoDiaSemana =  distribuicaoFornecedorRepository.verificarDistribuicaoDiaSemana(codigoProduto, idProdutoEdicao, diaSemana);
		
		Assert.assertFalse(verificaDistribuicaoDiaSemana);
	}
	
	@Test
	public void verificarRecolhimentoDiaSemana(){
		
		String codigoProduto  = "2a";
		Long idProdutoEdicao = 1L;
		DiaSemana diaSemana = DiaSemana.SEXTA_FEIRA;  
		
		boolean verificaRecolhimentoDiaSemana =  distribuicaoFornecedorRepository.verificarRecolhimentoDiaSemana(codigoProduto, idProdutoEdicao, diaSemana);
		
		Assert.assertFalse(verificaRecolhimentoDiaSemana);
	}
	
	@Test
	public void obterTodosOrdenadoId(){
						
		List<DistribuicaoFornecedor> distribuicaoFornecedor =  distribuicaoFornecedorRepository.obterTodosOrdenadoId();
		
		Assert.assertNotNull(distribuicaoFornecedor);
	}
	
	@Test
	public void excluirDadosFornecedor(){
							
		distribuicaoFornecedorRepository.excluirDadosFornecedor(fornecedorAcme);
	
	}

	@Test
	public void gravarAtualizarDadosFornecedor(){
		
		List<DistribuicaoFornecedor> lista = new ArrayList<DistribuicaoFornecedor>();
		lista.add(dinapSegunda);
		lista.add(dinapQuarta);
		
		distribuicaoFornecedorRepository.gravarAtualizarDadosFornecedor(lista);
	
	}

	
}
