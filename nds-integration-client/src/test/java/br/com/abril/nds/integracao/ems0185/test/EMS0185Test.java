package br.com.abril.nds.integracao.ems0185.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0185.route.EMS0185Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0185Test extends RouteNoTransactionTestTemplate {
	@Autowired
	private EMS0185Route rota;
	
	@Override
	public RouteTemplate getRoute() {
		return rota;
	}

}
