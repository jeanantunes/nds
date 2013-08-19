package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.EnderecoTransportador;

public class EnderecoTransportadorRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private EnderecoTransportadorRepositoryImpl enderecoTransportadorRepositoryImpl;

	@Test
	public void testarBuscarEnderecoPorEnderecoTransportador() {

		EnderecoTransportador enderecoTransportador;

		Long idEndereco = 1L;
		Long idTransportador = 2L;

		enderecoTransportador = enderecoTransportadorRepositoryImpl
				.buscarEnderecoPorEnderecoTransportador(idEndereco,
						idTransportador);

		Assert.assertNull(enderecoTransportador);

	}

	@Test
	public void testarRemoverEnderecosTransportador() {

		Set<Long> listaEnderecosRemover = new HashSet<Long>();
		listaEnderecosRemover.add(1L);
		listaEnderecosRemover.add(2L);
		listaEnderecosRemover.add(3L);

		enderecoTransportadorRepositoryImpl
				.removerEnderecosTransportador(listaEnderecosRemover);

		// Assert.assertNull();

	}

	@Test
	public void testarExcluirEnderecosPorIdTransportador() {

		Long id = 1L;

		enderecoTransportadorRepositoryImpl
				.excluirEnderecosPorIdTransportador(id);

		// Assert.assertNull();

	}

	@Test
	public void testarBuscarEnderecosTransportador() {

		List<EnderecoAssociacaoDTO> enderecosTransportador;

		Long id = 1L;
		Set<Long> idsIgnorar = new HashSet<Long>();

		enderecosTransportador = enderecoTransportadorRepositoryImpl
				.buscarEnderecosTransportador(id, idsIgnorar);

		Assert.assertNotNull(enderecosTransportador);

	}
	
	@Test
	public void testarBuscarEnderecosTransportadorIdsIgnorados() {

		List<EnderecoAssociacaoDTO> enderecosTransportador;

		Long id = 1L;
		Set<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(1L);
		idsIgnorar.add(2L);
		idsIgnorar.add(3L);

		enderecosTransportador = enderecoTransportadorRepositoryImpl
				.buscarEnderecosTransportador(id, idsIgnorar);

		Assert.assertNotNull(enderecosTransportador);

	}
	
	@Test
	public void testarVerificarEnderecoPrincipalTransportador() {
		
		boolean verificarEndereco;
		
		Long id = 1L;
		Set<Long> idsIgnorar = new HashSet<Long>();
		
		verificarEndereco = enderecoTransportadorRepositoryImpl.verificarEnderecoPrincipalTransportador(id, idsIgnorar);
		
		Assert.assertFalse(verificarEndereco);
	}
	
	@Test
	public void testarVerificarEnderecoPrincipalTransportadorIdsIgnorar() {
		
		boolean verificarEndereco;
		
		Long id = 1L;
		Set<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(1L);
		idsIgnorar.add(2L);
		idsIgnorar.add(3L);
		
		verificarEndereco = enderecoTransportadorRepositoryImpl.verificarEnderecoPrincipalTransportador(id, idsIgnorar);
		
		Assert.assertFalse(verificarEndereco);
	}

}
