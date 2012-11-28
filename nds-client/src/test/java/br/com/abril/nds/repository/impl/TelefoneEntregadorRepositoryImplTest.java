package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.repository.TelefoneEntregadorRepository;

public class TelefoneEntregadorRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	TelefoneEntregadorRepository telefoneEntregadorRepository;
	
	
	@Test
	public void removerTelefoneEntregadorPorIdEntregador(){
		Long idEntregador = 1L;
		
		telefoneEntregadorRepository.removerTelefoneEntregadorPorIdEntregador(idEntregador);
	}
	
	@Test
	public void removerTelefonesEntregador(){
		Collection<Long> listaTelefonesEntregador = new HashSet<Long>();
		listaTelefonesEntregador.add(1L);
		
		telefoneEntregadorRepository.removerTelefonesEntregador(listaTelefonesEntregador);
	}
	
	@Test
	public void buscarTelefonesEntregador(){
		Long idEntregador = 1L; 
		
		List<TelefoneAssociacaoDTO> telefoneAssociacaoDTOs  =
				telefoneEntregadorRepository.buscarTelefonesEntregador(idEntregador);
		
		Assert.assertNotNull(telefoneAssociacaoDTOs);
	}
}
