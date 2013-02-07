package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.repository.TelefoneTransportadorRepositoty;

public class TelefoneTransportadorRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	TelefoneTransportadorRepositoty telefoneTransportadorRepositoty;
	
	@Test
	public void pesquisarTelefonePrincipalTransportador(){
		Long idTransportador = 1L;
		
		Telefone telefone = telefoneTransportadorRepositoty.pesquisarTelefonePrincipalTransportador(idTransportador);
	}
	
	@Test
	public void removerTelefones(){
		Set<Long> listaTelefoneRemover = new HashSet<Long>();
		listaTelefoneRemover.add(1L);
		
		telefoneTransportadorRepositoty.removerTelefones(listaTelefoneRemover);
	}
	
	@Test
	public void buscarTelefonePorTelefoneTransportadorIdTelefone(){
		Long idTelefone = 1L;
		
		telefoneTransportadorRepositoty.buscarTelefonePorTelefoneTransportador(idTelefone, null);
	}
	
	@Test
	public void buscarTelefonePorTelefoneTransportadorIdTransportador(){
		Long idTransportador = 1L;
		
		telefoneTransportadorRepositoty.buscarTelefonePorTelefoneTransportador(null, idTransportador);
	}
	
	@Test
	public void excluirTelefonesTransportador(){
		Long id = 1L;
		
		telefoneTransportadorRepositoty.excluirTelefonesTransportador(id);
	}
	
	@Test
	public void buscarTelefonesTransportadorID(){
		Long id = 1L;
		
		List<TelefoneAssociacaoDTO> telefoneAssociacaoDTOs = 
				telefoneTransportadorRepositoty.buscarTelefonesTransportador(id, null);
		
		Assert.assertNotNull(telefoneAssociacaoDTOs);
	}
	
	@Test
	public void buscarTelefonesTransportadorIdsIgnorar(){
		Set<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(1L);
		
		List<TelefoneAssociacaoDTO> telefoneAssociacaoDTOs = 
				telefoneTransportadorRepositoty.buscarTelefonesTransportador(null, idsIgnorar);
		
		Assert.assertNotNull(telefoneAssociacaoDTOs);
	}

	@Test
	public void verificarTelefonePrincipalTransportadorId(){
		Long id = 1L;
		
		Boolean existePrincipal =  telefoneTransportadorRepositoty.verificarTelefonePrincipalTransportador(id, null);
		
		Assert.assertFalse(existePrincipal);
	}
	
	@Test
	public void verificarTelefonePrincipalTransportadorIdsIgnorar(){
		Set<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(1L);
		
		Boolean existePrincipal =  telefoneTransportadorRepositoty.verificarTelefonePrincipalTransportador(null, idsIgnorar);
		
		Assert.assertFalse(existePrincipal);
	}

}
