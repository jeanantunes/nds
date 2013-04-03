package br.com.abril.nds.integracao.ems0118.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0118.route.EMS0118Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0118TestUpdate extends RouteNoTransactionTestTemplate {
	@Autowired
	private EMS0118Route ems0118Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems0118Route;
	}
}
