package br.com.abril.nds.integracao.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.service.integracao.DistribuidorService;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration
@ContextConfiguration(locations={"classpath:spring/applicationContext-ndsi-test.xml"})
public class DistribuidorServiceTest {

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Test
	public void testarFindDistribuidor() {
		
		Distribuidor distribuidor = 
			this.distribuidorService.obter();
		
		Assert.assertNotNull(distribuidor);
	}
	
}
