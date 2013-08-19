package br.com.abril.nds.integracao.ems0124.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0124.route.EMS0124Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0124Test extends RouteTestTemplate {
	@Autowired
	private EMS0124Route rota;
	
	@Override
	public RouteTemplate getRoute() {
		return rota;
	}

}
