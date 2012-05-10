package br.com.abril.nds.integracao.engine.data;

public abstract class FileRouteTemplate extends RouteTemplate {
	public abstract String getInboundFolder();

	public abstract String getFileFilterExpression();

	public abstract String getArchiveFolder();

	public String getOutboundFolder() {
		return null;
	}
}