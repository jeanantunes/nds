package br.com.abril.nds.integracao.ems0120.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0120.route.EMS0120Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0120Test extends RouteTestTemplate {
	@Autowired
	private EMS0120Route rota;
	
	@Override
	public RouteTemplate getRoute() {
		return rota;
	}

}
