package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
}
