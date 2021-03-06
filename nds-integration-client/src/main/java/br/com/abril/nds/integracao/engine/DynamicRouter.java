package br.com.abril.nds.integracao.engine;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.data.CouchDBImportRouteTemplate;
import br.com.abril.nds.integracao.engine.data.DBImportRouteTemplate;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;
import br.com.abril.nds.integracao.engine.data.RouteTemplate;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.route.AbstractRoute;

@Component
public class DynamicRouter {
	
	@Resource(name="fixedLenghtContentBasedDataRouter")
	protected ContentBasedRouter FIXED_LENGHT_DATA_ROUTER;
	
	@Resource(name="couchDBImportDataRouter")
	protected ContentBasedRouter COUCH_DB_IMPORT_DATA_ROUTER;

	@Resource(name="fileOutputRouter")
	protected ContentBasedRouter fileOutputRouter;
	
	@Resource(name="dbImportDataRouter")
	protected ContentBasedRouter dbImportDataRouter;

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

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
		
		// ARQUIVOS COM CAMPOS DE TAMANHO FIXO
		if (route instanceof FixedLengthRouteTemplate) {
			FIXED_LENGHT_DATA_ROUTER.routeData(route);
		}
		else if (route instanceof CouchDBImportRouteTemplate) {
			COUCH_DB_IMPORT_DATA_ROUTER.routeData(route);
		}
		else if (route instanceof FileOutputRoute) {
			fileOutputRouter.routeData(route);
		}
		else if (route instanceof DBImportRouteTemplate) {
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