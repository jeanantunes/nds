<<<<<<< HEAD
package br.com.abril.nds.integracao.ems0131.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0131.route.EMS0131Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0131TestUpdate extends RouteNoTransactionTestTemplate {
	@Autowired
	private EMS0131Route rota;
	
	@Override
	public RouteTemplate getRoute() {
		return rota;
	}

}
=======
package br.com.abril.nds.integracao.ems0131.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0131.route.EMS0131Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0131TestUpdate extends RouteNoTransactionTestTemplate {
	@Autowired
	private EMS0131Route rota;
	
	@Override
	public RouteTemplate getRoute() {
		return rota;
	}

}
>>>>>>> refs/remotes/DGBTi/fase2
