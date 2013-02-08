package br.com.abril.nds.integracao.icd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.AbstractRoute;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;

@Component
public class DynamicRouterIcd {
	
	@Autowired
	private DBImportDataRouterIcd dBImportDataRouter;

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	public void route(RouteTemplateIcd route) {
		
		// LOG DE INICIALIZAÇÃO
		if (!(route instanceof AbstractRoute)) {
			ndsiLoggerFactory.resetLogger();
			ndsiLoggerFactory.getLogger().logBeginning(route);
		}
		
		// START
		route.onStart();
		
		// CONFIGURA A ROTA
		route.setupRoute();
		
		if (route instanceof DBImportRouteTemplateIcd) {
			dBImportDataRouter.routeData(route);
		}
		
		// END
		route.onEnd();
		
		// LOG DE FINALIZAÇÃO
		if (!(route instanceof AbstractRoute)) {
			ndsiLoggerFactory.getLogger().logEnd(route);
		}
	}	

}