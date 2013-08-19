package br.com.abril.nds.integracao.ems0133.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0133.route.EMS0133Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0133Test extends RouteTestTemplate {
	@Autowired
	private EMS0133Route rota;
	
	@Override
	public RouteTemplate getRoute() {
		return rota;
	}

}
