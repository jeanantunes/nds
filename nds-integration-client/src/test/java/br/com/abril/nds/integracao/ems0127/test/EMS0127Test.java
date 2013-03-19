package br.com.abril.nds.integracao.ems0127.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0127.route.EMS0127Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0127Test extends RouteNoTransactionTestTemplate {
	
	@Autowired
	private EMS0127Route route;
	
	@Override
	public RouteTemplate getRoute() {		
		return route;
	}
	
}
