package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.SocioCota;
import br.com.abril.nds.repository.SocioCotaRepository;

public class SocioCotaRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	SocioCotaRepository socioCotaRepository;
	
	@Test
	public void obterSocioCotaPorIdCota(){
		Long idCota = 1L;
		
		List<SocioCota> socioCota = socioCotaRepository.obterSocioCotaPorIdCota(idCota);
		
		Assert.assertNotNull(socioCota); 
	}
	
	@Test
	public void existeSocioPrincipalCota(){
		Long idCota = 1L;
		
		Boolean socioCota = socioCotaRepository.existeSocioPrincipalCota(idCota);
		
		Assert.assertFalse(socioCota); 
	}

}
