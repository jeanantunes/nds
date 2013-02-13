package br.com.abril.nds.integracao.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.log.NdsServerLoggerFactory;

@Component
public class DynamicRouterServer {
	
	@Autowired
	private DBImportDataRouter dbImportDataRouter;

	@Autowired
	private NdsServerLoggerFactory ndsiLoggerFactory;

	public void route(RouteTemplate route) {
		
		// LOG DE INICIALIZAÇÃO
		if (!(route instanceof AbstractRoute)) {
			ndsiLoggerFactory.resetLogger();
			ndsiLoggerFactory.getLogger().logBeginning(route);
		}
		
		// START
		route.onStart();
		
		// CONFIGURA A ROTA
		route.setupRoute();
				
		if (route instanceof DBImportRouteTemplate) {
			dbImportDataRouter.routeData(route);
		}
		
		// END
		route.onEnd();
		
		// LOG DE FINALIZAÇÃO
		if (!(route instanceof AbstractRoute)) {
			ndsiLoggerFactory.getLogger().logEnd(route);
		}
	}	
}