package br.com.abril.nds.converters;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.controllers.interceptor.ValidacaoInterceptor;
import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.converter.ConversionError;
import br.com.caelum.vraptor.ioc.RequestScoped;

@Convert(Long.class)
@RequestScoped
public class LongConverter implements Converter<Long> {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(LongConverter.class);
	 
	@Override
	public Long convert(String value, Class<? extends Long> type, ResourceBundle bundle) {
    try {
    	
    	
		if (isNullOrEmpty(value)) {
			return null;
		}
		if ("null".equalsIgnoreCase(value)) {
			return null;
		}
		if ("NaN".equalsIgnoreCase(value)) {
			return null;
		}
		if ( value.toLowerCase().startsWith("selecione")) {
			return null;
		}
		if ("undefined".equalsIgnoreCase(value)) {
			return null;
		}
		return Long.parseLong(value.trim());
    } catch (Exception e ) {
    	LOGGER.warn("ERRO CONVERSAO LONG='"+value+"'",e);
    	throw new ConversionError(MessageFormat.format(bundle.getString("is_not_a_valid_integer"), value));    
    }
	
	}

}


