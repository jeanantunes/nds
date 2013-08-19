package br.com.abril.nds.integracao.ems0123.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0123.route.EMS0123Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0123Test extends RouteTestTemplate {
	@Autowired
	private EMS0123Route rota;
	
	@Override
	public RouteTemplate getRoute() {
		return rota;
	}

}
