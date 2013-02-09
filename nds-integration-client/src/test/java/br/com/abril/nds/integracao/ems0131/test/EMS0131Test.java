package br.com.abril.nds.integracao.ems0131.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0131.route.EMS0131Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0131Test extends RouteTestTemplate {
	@Autowired
	private EMS0131Route rota;
	
	@Override
	public RouteTemplate getRoute() {
		return rota;
	}

}
