package br.com.abril.nds.integracao.ems0112.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0112.route.EMS0112Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0112TestUpdate extends RouteNoTransactionTestTemplate {
	@Autowired
	private EMS0112Route ems0112Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems0112Route;
	}
}
