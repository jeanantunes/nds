package br.com.abril.nds.repository.impl;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.pdv.GeradorFluxoPDV;

public class GeradorFluxoPDVRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private GeradorFluxoPDVRepositoryImpl geradorFluxoPDVRepositoryImpl;
	
	@Test
	public void testarObterGeradorFluxoPDV() {
		
		GeradorFluxoPDV geradorFluxoPDV;
		
		Long idPDV = 1L;
		
		geradorFluxoPDV = geradorFluxoPDVRepositoryImpl.obterGeradorFluxoPDV(idPDV);
		
		Assert.assertNull(geradorFluxoPDV);
		
	}

}
