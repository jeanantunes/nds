package br.com.abril.nds.integracao.ems0125.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0125.route.EMS0125Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0125Test extends RouteNoTransactionTestTemplate {
	@Autowired
	private EMS0125Route ems0125Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems0125Route;
	}	
}
