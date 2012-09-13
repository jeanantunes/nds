package br.com.abril.nds.integracao.ems0198.test;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0198.route.EMS0198Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;

public class EMS0198Test extends RouteNoTransactionTestTemplate {
	
	@Autowired
	private EMS0198Route route;
	
	@Override
	public RouteTemplate getRoute() {
		return this.route;
	}

	@Override
	public void test() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(2012, Calendar.MAY, 18);
		
		route.execute("Erick", calendar.getTime());

	}
}

