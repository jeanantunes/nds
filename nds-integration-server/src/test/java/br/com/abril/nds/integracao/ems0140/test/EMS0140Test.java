package br.com.abril.nds.integracao.ems0140.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0140.route.EMS0140Route;
import br.com.abril.nds.integracao.route.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0140Test extends RouteTestTemplate {
	
	@Autowired
	private EMS0140Route ems0140Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems0140Route;
	}
	
	@Test
	@Override
	public void test() {
		
		getRoute().execute("test");
		
		return;
		
	} 

}
