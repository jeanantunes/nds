package br.com.abril.nds.integracao.ems0117.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0117.route.EMS0117Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0117Test extends RouteNoTransactionTestTemplate {
	@Autowired
	private EMS0117Route ems0117Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems0117Route;
	}
}
