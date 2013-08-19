package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.estoque.CobrancaControleConferenciaEncalheCota;
import br.com.abril.nds.repository.CobrancaControleConferenciaEncalheCotaRepository;


public class CobrancaControleConferenciaEncalheCotaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private CobrancaControleConferenciaEncalheCotaRepository cobrancaControleConferenciaEncalheCotaRepository;
	
	@Test 
	public void obterCobrancaControleConferenciaEncalheCotaTest(){
		
		Long idControleConferenciaEncalheCota = 1L;
		
		List <CobrancaControleConferenciaEncalheCota> listaCobrancaControleConferenciaEncalheCota
		= cobrancaControleConferenciaEncalheCotaRepository.obterCobrancaControleConferenciaEncalheCota(idControleConferenciaEncalheCota);
		
		Assert.assertNotNull(listaCobrancaControleConferenciaEncalheCota);
	}
	

}
