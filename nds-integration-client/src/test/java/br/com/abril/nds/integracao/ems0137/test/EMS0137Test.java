package br.com.abril.nds.integracao.ems0137.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0137.route.EMS0137Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0137Test extends RouteNoTransactionTestTemplate {
	
	@Autowired
	private EMS0137Route route;
	
	@Override
	public RouteTemplate getRoute() {		
		return route;
	}
	
}
