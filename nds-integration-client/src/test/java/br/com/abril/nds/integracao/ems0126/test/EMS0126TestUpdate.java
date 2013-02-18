package br.com.abril.nds.integracao.ems0126.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0126.route.EMS0126Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0126TestUpdate extends RouteNoTransactionTestTemplate{
	/**
	 * @author Jones.Costa
	 * @version 1.0
	 */
	@Autowired
	private EMS0126Route ems0126Route;

	@Override
	public RouteTemplate getRoute() {
		
		return ems0126Route;
	}
	

}
