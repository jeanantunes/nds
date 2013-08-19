package br.com.abril.nds.integracao.route;

public interface BaseRouter {
	
	public <T extends RouteTemplate> void routeData(T inputModel);
	
}
