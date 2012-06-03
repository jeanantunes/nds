package br.com.abril.nds.integracao.engine;

import br.com.abril.nds.integracao.engine.data.FileRouteTemplate;

public abstract class FileOutputRoute extends FileRouteTemplate {

	@Override
	public String getFileFilterExpression() {
		return null;
	}

	@Override
	public void setupTypeMapping() {
		
	}
}