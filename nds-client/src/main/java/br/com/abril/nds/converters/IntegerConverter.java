package br.com.abril.nds.converters;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.converter.ConversionError;
import br.com.caelum.vraptor.ioc.RequestScoped;

@Convert(Integer.class)
@RequestScoped
public class IntegerConverter implements Converter<Integer> {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(IntegerConverter.class);
	 
	@Override
	public Integer convert(String value, Class<? extends Integer> type, ResourceBundle bundle) {
	try {
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
		return Integer.parseInt(value);
	 } catch (Exception e ) {
	    	e.printStackTrace();
	    	LOGGER.error("ERRO CONVERSAO INTEGER='"+value+"'");
	    	throw new ConversionError(MessageFormat.format(bundle.getString("is_not_a_valid_integer"), value));    
	    }	
	}

}
