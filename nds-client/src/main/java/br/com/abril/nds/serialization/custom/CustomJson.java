package br.com.abril.nds.serialization.custom;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import br.com.caelum.vraptor.View;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.ioc.Component;

import com.fasterxml.jackson.module.hibernate.HibernateModule;

@Component
public class CustomJson implements View {

	private ObjectMapper mapper;
	private HttpServletResponse response;

	private Object obj;

	public CustomJson(HttpServletResponse response,Localization localization)
			throws IOException {

		this.response = response;
		mapper = new ObjectMapper();
		mapper.registerModule(new HibernateModule());
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);

		mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,
				false);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, false);
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES,
				false);
		
		
		Locale locale = localization.getLocale();
		if (locale == null) {
			locale = Locale.getDefault();
		}
		
		DateFormat df = DateFormat
				.getDateInstance(DateFormat.MEDIUM, locale);
		df.setLenient(false);
		mapper.setDateFormat(df);

	}

	public CustomJson from(Object obj) {
		this.obj = obj;
		return this;
	}

	public CustomJson serialize() {
		try {
			mapper.writeValue(response.getWriter(), this.obj);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

}
