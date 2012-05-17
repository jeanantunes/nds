package br.com.abril.nds.integracao.ems0109.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0109.route.EMS0109Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0109Test extends RouteTestTemplate {

	@Autowired
	private EMS0109Route route;
	
	@Override
	public RouteTemplate getRoute() {
		return this.route;
	}

}
