package br.com.abril.nds.integracao.ems0114.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0114.route.EMS0114Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0114Test extends RouteNoTransactionTestTemplate {
	@Autowired
	private EMS0114Route ems0114Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems0114Route;
	}	
}
