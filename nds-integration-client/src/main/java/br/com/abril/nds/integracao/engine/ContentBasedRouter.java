package br.com.abril.nds.integracao.engine;

import br.com.abril.nds.integracao.engine.data.RouteTemplate;

public interface ContentBasedRouter {
	public <T extends RouteTemplate> void routeData(T inputModel);
}
