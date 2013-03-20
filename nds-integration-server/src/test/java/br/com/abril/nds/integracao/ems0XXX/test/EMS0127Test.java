package br.com.abril.nds.integracao.ems0XXX.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0127.route.EMS0127Route;
import br.com.abril.nds.integracao.route.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0127Test extends RouteTestTemplate {
	
	@Autowired
	private EMS0127Route ems0127Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems0127Route;
	}
	
	@Test
	@Override
	public void test() {
		
		getRoute().execute("test");
		
		return;
		
	}
	
}