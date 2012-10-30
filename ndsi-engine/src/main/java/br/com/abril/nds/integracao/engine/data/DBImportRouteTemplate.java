package br.com.abril.nds.integracao.engine.data;

import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;

public abstract class DBImportRouteTemplate extends RouteTemplate {

	public abstract InterfaceEnum getInterfaceEnum();
	
	@Override
	public void setupTypeMapping() {
	}
}