package br.com.abril.nds.converters;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.converter.ConversionError;
import br.com.caelum.vraptor.ioc.ApplicationScoped;


@Convert(Boolean.class)
@ApplicationScoped
public class BooleanConverter implements Converter<Boolean> {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(BooleanConverter.class);
	 
	private static final Set<String> IS_TRUE  = new HashSet<String>(Arrays.asList("TRUE", "1", "YES", "Y", "ON"));
	private static final Set<String> IS_FALSE = new HashSet<String>(Arrays.asList("FALSE", "0", "NO", "N", "OFF"));

	public Boolean convert(String value, Class<? extends Boolean> type, ResourceBundle bundle) {
	try {
		
		if (isNullOrEmpty(value)) {
			return null;
		}
		if (isNullOrEmpty(value)) {
			return null;
		}
		if ("null".equalsIgnoreCase(value)) {
			return null;
		}
		if ( value.toLowerCase().startsWith("selecione")) {
			return null;
		}
		if ("undefined".equalsIgnoreCase(value)) {
			return null;
		}
		
		value = value.toUpperCase();
		
		if (matches(IS_TRUE, value)) {
			return true;
		} else if (matches(IS_FALSE, value)) {
			return false;
		}
		return false;
	} catch (Exception e ) {
 
    	LOGGER.warn("ERRO CONVERSAO Boolean='"+value+"'",e);
    	throw new ConversionError(MessageFormat.format(bundle.getString("is_not_a_valid_boolean"), value));    
    }	
		
		
	}

	private boolean matches(Set<String> words, String value) {
		return words.contains(value);
	}
}
