package br.com.abril.nds.integracao.ems0113.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0113.route.EMS0113Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0113Test extends RouteTestTemplate {
	@Autowired
	private EMS0113Route ems0113Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems0113Route;
	}
}
