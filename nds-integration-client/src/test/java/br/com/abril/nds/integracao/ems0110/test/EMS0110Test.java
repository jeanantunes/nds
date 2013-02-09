package br.com.abril.nds.integracao.ems0110.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0110.route.EMS0110Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0110Test extends RouteNoTransactionTestTemplate {
	@Autowired
	private EMS0110Route ems0110Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems0110Route;
	}
}