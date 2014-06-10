package br.com.abril.nds.serialization.custom;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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

@Component
public class CustomJson implements View {

	private ObjectMapper mapper;
	private HttpServletResponse response;

	private Object obj;	
	
	private Map<Class<?>, Collection<String>> toExclude = new HashMap<Class<?>, Collection<String>>();

	
	public CustomJson(HttpServletResponse response,Localization localization)
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
			
			
			 SimpleModule testModule = new SimpleModule("DateSQL");
			 
			 testModule.addSerializer( new SqlDateSerializer());
			 mapper.registerModule(testModule);
			
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
			
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);

			mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
			mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		}
	
	public CustomJson from(Object obj) {
		this.obj = obj;
		return this;
	}
	
	public CustomJson put(String name,Object obj) {
		this.obj = obj;
		return this;
	}
	
	public CustomJson exclude(Class<?> clazz, String... properties) {
	    toExclude.put(clazz, Arrays.asList(properties));
	    return this;
	}

	public CustomJson serialize() {
		try {
		    if (!toExclude.isEmpty()) {
		        mapper.registerModule(new PropertyExcludeModule(toExclude));
		    }
			mapper.writeValue(response.getWriter(), this.obj);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return this;
	}
	
	public static final class PropertyExcludeModule extends Module {

	    private static final Version MODULE_VERSION = new Version(1, 0, 0, null);
	    
	    private final Map<Class<?>, Collection<String>> toExclude;

        public PropertyExcludeModule(Map<Class<?>, Collection<String>> toExclude) {
            this.toExclude = toExclude;
        }

        @Override
        public String getModuleName() {
             return PropertyExcludeModule.class.getName();
        }

        @Override
        public Version version() {
            return MODULE_VERSION;
        }

        @Override
        public void setupModule(SetupContext context) {
           context.addBeanSerializerModifier(ExcludePropertyBeanSerializerModifier.excluding(toExclude));
        }
	    
	}
	
	public static final class ExcludePropertyBeanSerializerModifier extends BeanSerializerModifier {
	    
	    private final Map<Class<?>, Collection<String>> toExclude;
	    
	    private ExcludePropertyBeanSerializerModifier(Map<Class<?>, Collection<String>> toExclude) {
            this.toExclude = toExclude;
        }
	    
	    static ExcludePropertyBeanSerializerModifier excluding(Map<Class<?>, Collection<String>> toExclude) {
	        return new ExcludePropertyBeanSerializerModifier(toExclude);
        }
	    
	    public List<BeanPropertyWriter> changeProperties(
	            SerializationConfig config, BasicBeanDescription beanDesc,
	            List<BeanPropertyWriter> beanProperties) {
	       if (toExclude == null || toExclude.isEmpty()) {
	           return beanProperties;
	       }
	       
	       Collection<String> excludingProperties = toExclude.get(beanDesc.getBeanClass());
	        if (excludingProperties == null || excludingProperties.isEmpty()) {
	            return beanProperties;
	        }
	        
	        List<BeanPropertyWriter> toSerialize = new ArrayList<BeanPropertyWriter>(beanProperties.size());
	        for (BeanPropertyWriter beanProperty : beanProperties) {
                if (!excludingProperties.contains(beanProperty.getName())) {
                    toSerialize.add(beanProperty);
                }
            }
	        return toSerialize;
	    }
	    
	}

}
