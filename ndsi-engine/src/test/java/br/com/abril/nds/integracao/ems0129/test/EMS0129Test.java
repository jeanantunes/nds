package br.com.abril.nds.integracao.ems0129.test;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0129.route.EMS0129Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteNoTransactionTestTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0129Test extends RouteTestTemplate {
	
	@Autowired
	private EMS0129Route route;
	
	@Override
	public RouteTemplate getRoute() {
		return this.route;
	}

	@Override
	public void test() {
		
		Calendar calendar = Calendar.getInstance();
//		calendar.set(2012, Calendar.JUNE, 15);
		
		route.execute("Erick", calendar.getTime());

	}
}
