package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.repository.TelefoneRepository;

public class TelefoneRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@Test
	public void removerTelefonesTest(){
		Set<Long> idsTelefones = new HashSet<Long>();
		idsTelefones.add(1L);
		this.telefoneRepository.removerTelefones(idsTelefones);
	}
	
	@Test
	public void buscarTelefonesPessoaIdPessoa(){
		Long idPessoa = 1L;
		
		List<Telefone> telefones =  telefoneRepository.buscarTelefonesPessoa(idPessoa, null);
		
		Assert.assertNotNull(telefones);
	}
	
	@Test
	public void buscarTelefonesPessoaIdsIgnorar(){
		Set<Long> idsOgnorar  = new HashSet<Long>();
		idsOgnorar.add(1L);

		List<Telefone> telefones =  telefoneRepository.buscarTelefonesPessoa(null, idsOgnorar);
		
		Assert.assertNotNull(telefones);
	}
}
