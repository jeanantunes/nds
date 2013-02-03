package br.com.abril.nds.integracao.ems0112.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0112.route.EMS0112Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0112Test extends RouteTestTemplate {
	
	@Autowired
	private EMS0112Route rota;
	
	@Override
	public RouteTemplate getRoute() {
		return rota;
	}
}
