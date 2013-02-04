package br.com.abril.nds.integracao.ems0119.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0119.route.EMS0119Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0119Test extends RouteTestTemplate {
	
	@Autowired
	private EMS0119Route rota;
	
	@Override
	public RouteTemplate getRoute() {
		return rota;
	}
}
