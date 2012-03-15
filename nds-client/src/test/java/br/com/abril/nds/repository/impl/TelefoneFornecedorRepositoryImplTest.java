package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.TelefoneFornecedorRepository;

public class TelefoneFornecedorRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private TelefoneFornecedorRepository telefoneFornecedorRepository;
	
	@Test
	public void buscarTelefonesFornecedorTest(){
		this.telefoneFornecedorRepository.buscarTelefonesFornecedor(1L, null);
		
		Set<Long> set = new HashSet<Long>();
		set.add(2L);
		
		this.telefoneFornecedorRepository.buscarTelefonesFornecedor(1l, set);
	}
	
	@Test
	public void removerTelefonesFornecedorTest(){
		Set<Long> set = new HashSet<Long>();
		set.add(2L);
		
		this.telefoneFornecedorRepository.removerTelefonesFornecedor(set);
	}
}
