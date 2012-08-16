package br.com.abril.nds.integracao.ems0130.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0130.route.EMS0130Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0130Test extends RouteTestTemplate {
	
	@Autowired
	private EMS0130Route rota;
	
	@Override
	public RouteTemplate getRoute() {
		return rota;
	}

}
