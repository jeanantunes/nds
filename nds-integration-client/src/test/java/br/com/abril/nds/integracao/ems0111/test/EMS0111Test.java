package br.com.abril.nds.integracao.ems0111.test;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0111.route.EMS0111Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0111Test extends RouteTestTemplate{
	/**
	 * @author Jones.Costa
	 * @version 1.0
	 */
	@Autowired
	private EMS0111Route ems0111Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems0111Route;
	}

}
