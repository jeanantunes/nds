package br.com.abril.nds.integracao.ems0140.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0140.route.EMS0140Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0140Test extends RouteTestTemplate {
	
	@Autowired
	private EMS0140Route route;
	
	@Override
	public RouteTemplate getRoute() {
		return this.route;
	}

}
