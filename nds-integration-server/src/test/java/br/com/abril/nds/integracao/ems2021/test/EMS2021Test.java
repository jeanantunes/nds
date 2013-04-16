package br.com.abril.nds.integracao.ems2021.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems2021.route.EMS2021Route;
import br.com.abril.nds.integracao.route.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS2021Test extends RouteTestTemplate {
	
	@Autowired
	private EMS2021Route ems2021Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems2021Route;
	}
	
	@Test
	@Override
	public void test() {
		
		getRoute().execute("test");
		
		return;
		
	}
	
}