package br.com.abril.nds.integracao.ems0106.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0106.route.EMS0106Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0106Test extends RouteTestTemplate {
	
	@Autowired
	private EMS0106Route route;
	
	@Override
	public RouteTemplate getRoute() {
		return this.route;
	}
	
}
