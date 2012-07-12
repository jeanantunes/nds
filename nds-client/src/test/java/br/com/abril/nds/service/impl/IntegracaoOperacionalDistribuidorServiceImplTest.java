package br.com.abril.nds.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.server.model.DistribuidorServer;
import br.com.abril.nds.service.IntegracaoOperacionalDistribuidorService;

public class IntegracaoOperacionalDistribuidorServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private IntegracaoOperacionalDistribuidorService integracaoOperacionalDistribuidorService;
	
	@Test
	public void testObterInformacoesOperacionais() {
		
		DistribuidorServer distribuidorServer = 
			this.integracaoOperacionalDistribuidorService.obterInformacoesOperacionais();
		
		Assert.assertNotNull(distribuidorServer);
	}
	
	@Test
	public void testIntegrarInformacoesOperacionais() {
		
		DistribuidorServer distribuidorServer = new DistribuidorServer();
		
		this.integracaoOperacionalDistribuidorService.integrarInformacoesOperacionais(distribuidorServer);
	}

}
