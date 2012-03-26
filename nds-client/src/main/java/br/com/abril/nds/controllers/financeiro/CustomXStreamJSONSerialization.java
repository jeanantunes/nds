package br.com.abril.nds.controllers.financeiro;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.serialization.ProxyInitializer;
import br.com.caelum.vraptor.serialization.Serializer;
import br.com.caelum.vraptor.serialization.xstream.XStreamBuilder;
import br.com.caelum.vraptor.serialization.xstream.XStreamJSONSerialization;

@Component
public class CustomXStreamJSONSerialization extends XStreamJSONSerialization {

	public CustomXStreamJSONSerialization(HttpServletResponse response, TypeNameExtractor extractor,
				 ProxyInitializer initializer, XStreamBuilder builder) {
		
		super(response, extractor, initializer, builder);
	}
	
	@Override
	public <T> Serializer from(T object, String alias) {
		
		response.setContentType("text/plain");
        return getSerializer().from(object, alias);
	};

}
