package br.com.abril.nds.integracao.ems0197.test;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.integracao.ems0197.route.EMS0197Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.test.RouteTestTemplate;

public class EMS0197Test extends RouteTestTemplate {
	@Autowired
	private EMS0197Route ems0197Route;
	
	@Override
	public RouteTemplate getRoute() {
		return ems0197Route;
	}
	
	
	@Override
	public void test() {
		Calendar data = Calendar.getInstance();
		data.set(2012, Calendar.MAY, 18);
		ems0197Route.execute("Jones", data.getTime());
	}
	
	
}
