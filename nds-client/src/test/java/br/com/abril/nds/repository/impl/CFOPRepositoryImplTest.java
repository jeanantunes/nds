package br.com.abril.nds.repository.impl;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.repository.CFOPRepository;

public class CFOPRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private  CFOPRepository cfopRepository;
	
	@Test
	public void testarObterPorCodigo() {	
		
		CFOP cfop;
		
		String codigo = "1";
		
		cfop = cfopRepository.obterPorCodigo(codigo);
		
		Assert.assertNull(cfop);		
	}

}
