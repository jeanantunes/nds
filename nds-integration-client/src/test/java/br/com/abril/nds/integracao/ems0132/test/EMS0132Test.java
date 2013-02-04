package br.com.abril.nds.integracao.ems0132.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0132.route.EMS0132Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0132Test extends RouteTestTemplate {

	@Autowired
	private EMS0132Route ems132Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems132Route;
	}
	
}
