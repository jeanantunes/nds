package br.com.abril.nds.integracao.ems0107.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0107.route.EMS0107Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0107Test extends RouteTestTemplate {

	@Autowired
	private EMS0107Route route;
	
	@Override
	public RouteTemplate getRoute() {
		return this.route;
	}

}
