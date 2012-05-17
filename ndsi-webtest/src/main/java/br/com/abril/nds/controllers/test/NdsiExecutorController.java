package br.com.abril.nds.controllers.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.abril.nds.integracao.ems0116.route.EMS0116Route;
import br.com.abril.nds.integracao.ems0117.route.EMS0117Route;
import br.com.abril.nds.integracao.ems0118.route.EMS0118Route;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;

@Controller
public class NdsiExecutorController {
	@Autowired
	private ApplicationContext applicationContext;
	
	@RequestMapping("/inicial")
	public String inicial() {
		return "executeNdsi";
	}

	@RequestMapping("/executeNdsi")
	public void execute(String emsRoute, String userName) {
		try {
			RouteTemplate route = (RouteTemplate) applicationContext.getBean(Class.forName(emsRoute));
			
			route.execute(userName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@RequestMapping("/executeEMS0118")
	public void executeEMS0118 () {
		try {
			EMS0118Route route = (EMS0118Route) applicationContext.getBean(EMS0118Route.class);
			
			route.execute("Webtester");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@RequestMapping("/executeEMS0116")
	public void executeEMS0116 () {
		try {
			EMS0116Route route = (EMS0116Route) applicationContext.getBean(EMS0116Route.class);
			
			route.execute("Webtester");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@RequestMapping("/executeEMS0117")
	public void executeEMS0117 () {
		try {
			EMS0117Route route = (EMS0117Route) applicationContext.getBean(EMS0117Route.class);
			
			route.execute("Webtester");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
