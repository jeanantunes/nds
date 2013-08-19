package br.com.abril.nds.integracao.ems0135.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0135.route.EMS0135Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0135Test extends RouteNoTransactionTestTemplate {
	
	@Autowired
	private EMS0135Route route;
	

	@Override
	public RouteTemplate getRoute() {		
		return route;
	}

}
