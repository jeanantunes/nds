package br.com.abril.nds.serialization.custom;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import br.com.caelum.vraptor.View;
import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.serialization.ProxyInitializer;

import com.fasterxml.jackson.module.hibernate.HibernateModule;

@Component
public class CustomJson implements View {

	private ObjectMapper mapper;
	private HttpServletResponse response;

	private Object obj;

	public CustomJson(HttpServletResponse response,
			TypeNameExtractor extractor, ProxyInitializer initializer)
			throws IOException {
		
		this.response = response;
		mapper = new ObjectMapper();
		mapper.registerModule(new HibernateModule());
		mapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);

	}

	public CustomJson from(Object obj) {
		this.obj = obj;
		return this;
	}

	
	public CustomJson serialize(){
		try {
			mapper.writeValue(response.getOutputStream(), this.obj);
		}  catch (IOException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

}
