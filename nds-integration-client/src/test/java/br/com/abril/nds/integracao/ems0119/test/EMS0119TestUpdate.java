package br.com.abril.nds.integracao.ems0119.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0119.route.EMS0119Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0119TestUpdate extends RouteNoTransactionTestTemplate {
	@Autowired
	private EMS0119Route ems0119Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems0119Route;
	}
}
