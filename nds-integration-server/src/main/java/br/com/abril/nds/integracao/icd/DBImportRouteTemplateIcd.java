package br.com.abril.nds.integracao.icd;

import br.com.abril.nds.integracao.model.canonic.InterfaceEnum;

public abstract class DBImportRouteTemplateIcd extends RouteTemplateIcd {

	public abstract InterfaceEnum getInterfaceEnum();
	
	@Override
	public void setupTypeMapping() {
	}
}