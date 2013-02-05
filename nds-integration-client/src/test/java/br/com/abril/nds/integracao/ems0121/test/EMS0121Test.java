package br.com.abril.nds.integracao.ems0121.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0121.route.EMS0121Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0121Test extends RouteTestTemplate {
	@Autowired
	private EMS0121Route rota;
	
	@Override
	public RouteTemplate getRoute() {
		return rota;
	}

}
