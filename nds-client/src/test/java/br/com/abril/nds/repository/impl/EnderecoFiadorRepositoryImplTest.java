package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoFiador;
import br.com.abril.nds.repository.EnderecoFiadorRepository;

public class EnderecoFiadorRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private EnderecoFiadorRepository enderecoFiadorRepository;
	
	@Test
	public void buscaEnderecosFiadorIdFiador(){
		Long idFiador = 1L;
		
		List<EnderecoAssociacaoDTO> enderecoAssociacaoDTO = 
				enderecoFiadorRepository.buscaEnderecosFiador(idFiador, null);
		
		Assert.assertNotNull(enderecoAssociacaoDTO);
	}
	
	@Test
	public void buscaEnderecosFiadorIdsIgnorar(){
		
		Set<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(1L);
		
		List<EnderecoAssociacaoDTO> enderecoAssociacaoDTO = 
				enderecoFiadorRepository.buscaEnderecosFiador(null, idsIgnorar);
		
		Assert.assertNotNull(enderecoAssociacaoDTO);
	}
	
	@Test
	public void buscarEnderecosPessoaPorFiador(){
		Long idFiador = 1L;
		
		List<Endereco> endereco = enderecoFiadorRepository.buscarEnderecosPessoaPorFiador(idFiador);
		
		Assert.assertNotNull(endereco);
	}
	
	@Test
	public void buscarEnderecoPorEnderecoFiadorIdEndereco(){
		Long idEndereco = 1L;
		
		EnderecoFiador enderecoFiador = 
			enderecoFiadorRepository.buscarEnderecoPorEnderecoFiador(idEndereco, null);
		
	}
	
	@Test
	public void buscarEnderecoPorEnderecoFiadorIdFiador(){
		Long idFiador = 1L;
		
		EnderecoFiador enderecoFiador = 
			enderecoFiadorRepository.buscarEnderecoPorEnderecoFiador(null, idFiador);
		
	}
	
	@Test
	public void excluirEnderecosPorIdFiador(){
		Long idFiador = 1L;
		
			enderecoFiadorRepository.excluirEnderecosPorIdFiador(idFiador);
		
	}
	
	@Test
	public void excluirEnderecosFiador(){
		List<Long> idsEnderecoFiador = new ArrayList<>();
		idsEnderecoFiador.add(1L);
		
			enderecoFiadorRepository.excluirEnderecosFiador(idsEnderecoFiador);
		
	}
	
	@Test
	public void verificarEnderecoPrincipalFiadorId(){
		Long id = 1L;
		
		boolean	verificarEnderecoPrincipalFiador = 
				enderecoFiadorRepository.verificarEnderecoPrincipalFiador(id, null);
		
		Assert.assertFalse(verificarEnderecoPrincipalFiador);
		
	}
	
	@Test
	public void verificarEnderecoPrincipalFiadorIdignorar(){
		Set<Long> idsIgnorar = new HashSet<Long>();
		idsIgnorar.add(1L);
		
		boolean	verificarEnderecoPrincipalFiador = 
				enderecoFiadorRepository.verificarEnderecoPrincipalFiador(null, idsIgnorar);
		
		Assert.assertFalse(verificarEnderecoPrincipalFiador);
		
	}
	
	@Test
	public void buscaPrincipal(){
		Long idFiador = 1L;
		
		Endereco endereco =	enderecoFiadorRepository.buscaPrincipal(idFiador);
		
			
	}

	
	
	
}
