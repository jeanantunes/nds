package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Garantia;
import br.com.abril.nds.repository.GarantiaRepository;

public class GarantiaRepositoryImplTest extends AbstractRepositoryImplTest {

	
	@Autowired
	
	GarantiaRepository garantiaRepository;
	
	@Test
	public void obterGarantiasFiadorIdFiador(){
		
		Long idFiador = 1L;
		
		List <Garantia> garantia = garantiaRepository.obterGarantiasFiador(idFiador, null);
		
		Assert.assertNotNull(garantia);
		
	}
	
	@Test
	public void obterGarantiasFiadorIdsIgnorar(){
		
		Set <Long> idsIgnorar = new HashSet<>(); 
		idsIgnorar.add(1L);
		
		List <Garantia> garantia = garantiaRepository.obterGarantiasFiador(null, idsIgnorar);
		
		Assert.assertNotNull(garantia);
		
	}
	
	@Test
	public void removerGarantias(){
		
		Set <Long> idsGarantia = new HashSet<>(); 
		idsGarantia.add(1L);
		
		garantiaRepository.removerGarantias(idsGarantia);
		
		
	}
	
	@Test
	public void removerGarantiasPorFiador(){
		
		Long idFiador = 1L;
		
		garantiaRepository.removerGarantiasPorFiador(idFiador);
		
		
	}
}
