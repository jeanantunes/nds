package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Fornecedor;

public class DistribuicaoFornecedorRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private DistribuicaoFornecedorRepositoryImpl distribuicaoFornecedorRepository;
	
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
	
		
	
}
