package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Veiculo;
import br.com.abril.nds.repository.VeiculoRepository;

public class VeiculoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private VeiculoRepository veiculoRepository;

	@Test
	public void buscarVeiculosPorTransportador() {
		Set<Long> idsIgnorar = null;

		List<Veiculo> lista = veiculoRepository.buscarVeiculosPorTransportador(
				null, idsIgnorar, "", "");

		Assert.assertNotNull(lista);
	}
	
	@Test
	public void buscarVeiculosPorTransportadorPorIdsIgnorar() {
		Set<Long> idsIgnorar = new HashSet<>();
		idsIgnorar.add(1L);
		idsIgnorar.add(2L);

		List<Veiculo> lista = veiculoRepository.buscarVeiculosPorTransportador(
				1L, idsIgnorar, "", "");

		Assert.assertNotNull(lista);
	}
	
	
	@Test
	public void buscarVeiculosPorTransportadorSortnamePlaca() {
		Set<Long> idsIgnorar = null;

		List<Veiculo> lista = veiculoRepository.buscarVeiculosPorTransportador(
				1L, idsIgnorar, "placa", "");

		Assert.assertNotNull(lista);
	}
	
	@Test
	public void buscarVeiculosPorTransportadorSortnameTipoVeiculo() {
		Set<Long> idsIgnorar = null;

		List<Veiculo> lista = veiculoRepository.buscarVeiculosPorTransportador(
				1L, idsIgnorar, "tipoVeiculo", "");

		Assert.assertNotNull(lista);
	}
	
	@Test
	public void buscarVeiculosPorTransportadorSortorderAsc() {
		Set<Long> idsIgnorar = null;

		List<Veiculo> lista = veiculoRepository.buscarVeiculosPorTransportador(
				1L, idsIgnorar, "", "asc");

		Assert.assertNotNull(lista);
	}
	
	@Test
	public void buscarVeiculosPorTransportadorSortorderDesc() {
		Set<Long> idsIgnorar = null;

		List<Veiculo> lista = veiculoRepository.buscarVeiculosPorTransportador(
				1L, idsIgnorar, "", "desc");

		Assert.assertNotNull(lista);
	}
	
	@Test
	public void removerPorId(){
		veiculoRepository.removerPorId(1L);
	}
	
	@Test
	public void removerVeiculos(){
		Set<Long> listaVeiculosRemover = null;
		veiculoRepository.removerVeiculos(1L, listaVeiculosRemover);
	}
	
	@Test
	public void removerVeiculosPorListaVeiculosRemover(){
		Set<Long> listaVeiculosRemover = new HashSet<>();
		listaVeiculosRemover.add(1L);
		listaVeiculosRemover.add(2L);
		veiculoRepository.removerVeiculos(1L, listaVeiculosRemover);
	}

}
