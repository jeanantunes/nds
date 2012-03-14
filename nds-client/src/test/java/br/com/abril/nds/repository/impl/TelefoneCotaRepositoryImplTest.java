package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.TelefoneCotaRepository;

public class TelefoneCotaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private TelefoneCotaRepository telefoneCotaRepository;
	
	@Test
	public void buscarTelefonesCotaTest(){
		
		this.telefoneCotaRepository.buscarTelefonesCota(1L, null);
		
		Set<Long> set = new HashSet<Long>();
		set.add(2L);
		
		this.telefoneCotaRepository.buscarTelefonesCota(1L, set);
	}
	
	@Test
	public void removerTelefonesCotaTest(){
		
		Set<Long> set = new HashSet<Long>();
		set.add(2L);
		
		this.telefoneCotaRepository.removerTelefonesCota(set);
	}
}
