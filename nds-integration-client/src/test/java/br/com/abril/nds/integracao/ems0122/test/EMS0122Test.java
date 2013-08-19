package br.com.abril.nds.integracao.ems0122.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0122.route.EMS0122Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0122Test extends RouteTestTemplate {
	@Autowired
	private EMS0122Route rota;
	
	@Override
	public RouteTemplate getRoute() {
		return rota;
	}

}
