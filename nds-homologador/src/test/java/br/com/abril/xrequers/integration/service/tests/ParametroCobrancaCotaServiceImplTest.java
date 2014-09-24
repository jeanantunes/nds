package br.com.abril.xrequers.integration.service.tests;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ParametroCobrancaCotaDTO;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.xrequers.integration.repository.tests.AbstractRepositoryTest;

public class ParametroCobrancaCotaServiceImplTest extends AbstractRepositoryTest {
	
	@Autowired
	private ParametroCobrancaCotaService parametroCobrancaCotaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	
	@Test
	public void test_obter_fornecedor_padrao() {
		
		ParametroCobrancaCotaDTO param = parametroCobrancaCotaService.obterDadosParametroCobrancaPorCota(1034L);
		
		Assert.assertNotNull(param);
		
		
	}
	

}
