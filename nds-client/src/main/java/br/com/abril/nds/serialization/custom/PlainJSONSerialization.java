package br.com.abril.nds.serialization.custom;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.View;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.serialization.JSONSerialization;
import br.com.caelum.vraptor.serialization.Serializer;

@Component
public class PlainJSONSerialization implements View {

	private JSONSerialization serialization;

	private HttpServletResponse response;

	public PlainJSONSerialization(JSONSerialization serialization, HttpServletResponse response) {
		this.serialization = serialization;
		this.response = response;
	}

	public Serializer from(Object objeto, String alias) {
		Serializer serializer = serialization.from(objeto, alias);
		response.setHeader("Content-Type", "text/plain");
		return serializer;
	}
	
}
