package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.AssociacaoVeiculoMotoristaRota;
import br.com.abril.nds.repository.AssociacaoVeiculoMotoristaRotaRepository;


public class AssociacaoVeiculoMotoristaRotaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired 
	private AssociacaoVeiculoMotoristaRotaRepository associacaoVeiculoMotoristaRotaRepository;
	
	@Test
	public void removerAssociacaoPorIdTest (){
		Set<Long> ids = new HashSet<Long>();
		ids.add(1L);
		ids.add(2L);
		associacaoVeiculoMotoristaRotaRepository.removerAssociacaoPorId(ids);
	}
	
	@Test
	public void removerAssociacaoTransportadorTest (){
		long id = 1L;
		associacaoVeiculoMotoristaRotaRepository.removerAssociacaoTransportador(id);
	}
	
	@Test
	public void buscarAssociacoesTransportadorTest(){
		long idTransportador = 1;
	 
		List<AssociacaoVeiculoMotoristaRota> listaAssociacaoVeiculoMotoristaRotas = associacaoVeiculoMotoristaRotaRepository.buscarAssociacoesTransportador(idTransportador, null, null, null);
		
		Assert.assertNotNull(listaAssociacaoVeiculoMotoristaRotas);
		
	}
	
	@Test
	public void buscarAssociacoesTransportadorNuloTest(){
			 
		List<AssociacaoVeiculoMotoristaRota> listaAssociacaoVeiculoMotoristaRotas = associacaoVeiculoMotoristaRotaRepository.buscarAssociacoesTransportador(null, null, null, null);
		
		Assert.assertNotNull(listaAssociacaoVeiculoMotoristaRotas);
		
	}
	
	@Test
	public void buscarAssociacoesTransportadorIdsIgnoradosTest(){
		Set<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(2L);
		idsIgnorar.add(3L);
		
		List<AssociacaoVeiculoMotoristaRota> listaAssociacaoVeiculoMotoristaRotas = associacaoVeiculoMotoristaRotaRepository.buscarAssociacoesTransportador(null, idsIgnorar, null,null);
		
		Assert.assertNotNull(listaAssociacaoVeiculoMotoristaRotas);
		
	}
	
	@Test
	public void buscarAssociacoesTransportadorSortNameRoteiroTest(){
		String SortName = "roteiro";
		
		List<AssociacaoVeiculoMotoristaRota> listaAssociacaoVeiculoMotoristaRotas = associacaoVeiculoMotoristaRotaRepository.buscarAssociacoesTransportador(null, null, SortName, null);
		
		Assert.assertNotNull(listaAssociacaoVeiculoMotoristaRotas);
		
	}
	
	@Test
	public void buscarAssociacoesTransportadorSortNamePlacaTest(){
		String SortName = "placa";
		
		List<AssociacaoVeiculoMotoristaRota> listaAssociacaoVeiculoMotoristaRotas = associacaoVeiculoMotoristaRotaRepository.buscarAssociacoesTransportador(null, null, SortName, null);
		
		Assert.assertNotNull(listaAssociacaoVeiculoMotoristaRotas);
		
	}
	
	@Test
	public void buscarAssociacoesTransportadorSortNameMotoristaTest(){
		String SortName = "motorista";
		
		List<AssociacaoVeiculoMotoristaRota> listaAssociacaoVeiculoMotoristaRotas = associacaoVeiculoMotoristaRotaRepository.buscarAssociacoesTransportador(null, null, SortName, null);
		
		Assert.assertNotNull(listaAssociacaoVeiculoMotoristaRotas);
		
	}
	
	@Test
	public void buscarAssociacoesTransportadorSortNameCnhTest(){
		String SortName = "cnh";
		
		List<AssociacaoVeiculoMotoristaRota> listaAssociacaoVeiculoMotoristaRotas = associacaoVeiculoMotoristaRotaRepository.buscarAssociacoesTransportador(null, null, SortName, null);
		
		Assert.assertNotNull(listaAssociacaoVeiculoMotoristaRotas);
		
	}
	
	@Test
	public void buscarAssociacoesTransportadorSortNameRotaTest(){
		String SortName = "rota";
		
		List<AssociacaoVeiculoMotoristaRota> listaAssociacaoVeiculoMotoristaRotas = associacaoVeiculoMotoristaRotaRepository.buscarAssociacoesTransportador(null, null, SortName, null);
		
		Assert.assertNotNull(listaAssociacaoVeiculoMotoristaRotas);
		
	}
	
	@Test
	public void buscarAssociacoesTransportadorSortNameNullTest(){
		String SortName = null;
		
		List<AssociacaoVeiculoMotoristaRota> listaAssociacaoVeiculoMotoristaRotas = associacaoVeiculoMotoristaRotaRepository.buscarAssociacoesTransportador(null, null, SortName, null);
		
		Assert.assertNotNull(listaAssociacaoVeiculoMotoristaRotas);
		
	}
	
	@Test
	public void buscarAssociacoesTransportadorSortOrderAscTest(){
		String sortOrder = "asc";
		
		List<AssociacaoVeiculoMotoristaRota> listaAssociacaoVeiculoMotoristaRotas = associacaoVeiculoMotoristaRotaRepository.buscarAssociacoesTransportador(null, null, null, sortOrder);
		
		Assert.assertNotNull(listaAssociacaoVeiculoMotoristaRotas);
		
	}
	
	@Test
	public void buscarAssociacoesTransportadorSortOrderDescTest(){
		String sortOrder = "desc";
		
		List<AssociacaoVeiculoMotoristaRota> listaAssociacaoVeiculoMotoristaRotas = associacaoVeiculoMotoristaRotaRepository.buscarAssociacoesTransportador(null, null, null, sortOrder);
		
		Assert.assertNotNull(listaAssociacaoVeiculoMotoristaRotas);
		
	}
	
	@Test
	public void buscarIdsRotasPorAssociacaoTest(){
		Set<Long> associacoesRemovidas = new HashSet<Long>();
		associacoesRemovidas.add(1L);
		associacoesRemovidas.add(2L);
		
		List<Long> listaIdsRotas = associacaoVeiculoMotoristaRotaRepository.buscarIdsRotasPorAssociacao(associacoesRemovidas);
		
		Assert.assertNotNull(listaIdsRotas);
	}
	
	@Test
	public void verificarAssociacaoMotoristaIdMotoristaTest (){
		Long idMotorista =  1L;
		
		boolean contemAssociacaoComMotorista = associacaoVeiculoMotoristaRotaRepository.verificarAssociacaoMotorista(idMotorista, null); 
		
		Assert.assertFalse(contemAssociacaoComMotorista);
	}
	@Test
	public void verificarAssociacaoMotoristaIdIgnorarTest (){
		Set<Long> idIgnorar = new HashSet<Long>();
		idIgnorar.add(2L);
		
		boolean contemAssociacaoComMotorista = associacaoVeiculoMotoristaRotaRepository.verificarAssociacaoMotorista(null, idIgnorar); 
		
		Assert.assertFalse(contemAssociacaoComMotorista);
	}
	
	@Test
	public void verificarAssociacaoVeiculoIdVeiculoTest (){
		Long idVeiculo =  1L;
		
		boolean contemAssociacaoComVeiculo = associacaoVeiculoMotoristaRotaRepository.verificarAssociacaoVeiculo(idVeiculo, null);
		
		Assert.assertFalse(contemAssociacaoComVeiculo);
	}
	
	@Test
	public void verificarAssociacaoVeiculoIdIgnorarTest (){
		Set<Long> idIgnorar = new HashSet<Long>();
		idIgnorar.add(2L);

		
		boolean contemAssociacaoComVeiculo = associacaoVeiculoMotoristaRotaRepository.verificarAssociacaoVeiculo(null, idIgnorar);
		
		Assert.assertFalse(contemAssociacaoComVeiculo);
	}

	@Test
	public void verificarAssociacaoRotaRoteiroTest(){
		Long idRota =  1L;
		
		boolean contemAssociacaoComRota = associacaoVeiculoMotoristaRotaRepository.verificarAssociacaoRotaRoteiro(idRota);
		
		Assert.assertFalse(contemAssociacaoComRota);
	}
}
