package br.com.abril.nds.serialization.custom;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import br.com.abril.nds.serialization.custom.CustomJson.PropertyExcludeModule;
import br.com.caelum.vraptor.View;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.ioc.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.SqlDateSerializer;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.module.hibernate.HibernateModule;
@Component
public class CustomMapJson implements View {

	private ObjectMapper mapper;
	private HttpServletResponse response;

	
	private Map<String,Object> map = new HashMap<String, Object>();
	
	private Map<Class<?>, Collection<String>> toExclude = new HashMap<Class<?>, Collection<String>>();

	public CustomMapJson(HttpServletResponse response,Localization localization)
			throws IOException {

		this.response = response;
		mapper = new ObjectMapper();
		mapper.registerModule(new Hibernate4Module());
		Locale locale = localization.getLocale();
		if (locale == null) {
			locale = Locale.getDefault();
		}
		
		DateFormat df = DateFormat
				.getDateInstance(DateFormat.MEDIUM, locale);
		df.setLenient(false);
		mapper.setDateFormat(df);
		
		
		 SimpleModule testModule = new SimpleModule("DateSQL", new Version(1, 0, 0, null));
		 
		 testModule.addSerializer( new SqlDateSerializer());
		 mapper.registerModule(testModule);
		
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
		
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);

		mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
		mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES,
				false);
		
		

	}
	
	public CustomMapJson put(String key,Object value) {
		map.put(key, value);
		return this;
	}
	
	public CustomMapJson exclude(Class<?> clazz, String... properties) {
	      toExclude.put(clazz, Arrays.asList(properties));
	      return this;
	}

	public CustomMapJson serialize() {
		try {
			if (!toExclude.isEmpty()) {
			    mapper.registerModule(new PropertyExcludeModule(toExclude));
			}
		    mapper.writeValue(response.getWriter(), this.map);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

}
