package br.com.abril.nds.integracao.ems0138.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0138.route.EMS0138Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0138Test extends RouteNoTransactionTestTemplate {
	
	@Autowired
	private EMS0138Route route;
	
	@Override
	public RouteTemplate getRoute() {		
		return route;
	}
	
	@Test
	public void test() {
		getRoute().execute("test");
	}
	
}
