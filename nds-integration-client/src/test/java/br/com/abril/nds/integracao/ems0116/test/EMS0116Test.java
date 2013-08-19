package br.com.abril.nds.integracao.ems0116.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0116.route.EMS0116Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0116Test extends RouteNoTransactionTestTemplate {
	@Autowired
	private EMS0116Route ems0116Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems0116Route;
	}
}