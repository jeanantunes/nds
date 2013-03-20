package br.com.abril.nds.integracao.ems0XXX.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0128.route.EMS0128Route;
import br.com.abril.nds.integracao.route.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0128Test extends RouteTestTemplate {
	
	@Autowired
	private EMS0128Route ems0128Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems0128Route;
	}
	
	@Test
	@Override
	public void test() {
		
		getRoute().execute("test");
		
		return;
		
	}
	
}