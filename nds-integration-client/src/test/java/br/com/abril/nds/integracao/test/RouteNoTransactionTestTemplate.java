package br.com.abril.nds.integracao.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.spring.NdsiRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={NdsiRunner.SPRING_FILE_LOCATION})
public abstract class RouteNoTransactionTestTemplate extends AbstractJUnit4SpringContextTests {
	/**
	 * Obtem a Rota a ser testada
	 * 
	 * @return Implementacao da rota
	 */
	public abstract RouteTemplate getRoute();
	
	@Test
	public void test() {
		getRoute().execute("test");
	}
}
