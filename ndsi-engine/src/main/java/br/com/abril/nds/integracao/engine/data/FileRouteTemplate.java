package br.com.abril.nds.integracao.engine.data;

import br.com.abril.nds.model.cadastro.TipoParametroSistema;

public abstract class FileRouteTemplate extends RouteTemplate {
	public final String getInboundFolder() {
		return null ; //(String) getParameters().get(TipoParametroSistema.PATH_INTERFACE_MDC_IMPORTACAO.toString());
	}

	public String getFileFilterExpression() {
		return null;
	}

	public final String getArchiveFolder() {
		return null ; //(String) getParameters().get(TipoParametroSistema.PATH_INTERFACE_MDC_BACKUP.toString());
	}

	public final String getOutboundFolder() {
		return null ; //(String) getParameters().get(TipoParametroSistema.PATH_INTERFACE_MDC_EXPORTACAO.toString());
	}
}