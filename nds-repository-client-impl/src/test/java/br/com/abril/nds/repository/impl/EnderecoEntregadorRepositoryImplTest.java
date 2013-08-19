package br.com.abril.nds.repository.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.EnderecoEntregadorRepository;

public class EnderecoEntregadorRepositoryImplTest extends AbstractRepositoryImplTest {

	
	@Autowired
	private EnderecoEntregadorRepository enderecoEntregadorRepository;
	
	@Test
	public void removerEnderecosEntregadorPorIdEntregador(){
		Long idEntregador = 1L;
		
		enderecoEntregadorRepository.removerEnderecosEntregadorPorIdEntregador(idEntregador);
	}
	
}
