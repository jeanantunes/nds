package br.com.abril.nds.service.impl;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.CotaService;

public class CotaServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private CotaService cotaService;
	
	@Before
	public void setup() {
		
		//this.cotaService.obterEnderecosPorIdCota(idCota)
	}
}
