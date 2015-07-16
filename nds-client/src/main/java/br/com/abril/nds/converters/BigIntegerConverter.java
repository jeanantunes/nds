package br.com.abril.nds.converters;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.converter.ConversionError;
import br.com.caelum.vraptor.ioc.RequestScoped;

@Convert(BigInteger.class)
@RequestScoped
public class BigIntegerConverter implements Converter<BigInteger> {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(IntegerConverter.class);
	 
	@Override
	public BigInteger convert(String value, Class<? extends BigInteger> type, ResourceBundle bundle) {
	try {
		if (isNullOrEmpty(value)) {
			return null;
		}
		if ("null".equalsIgnoreCase(value)) {
			return null;
		}
		if ("SELECIONE".equalsIgnoreCase(value)) {
			return null;
		}
		if ("undefined".equalsIgnoreCase(value)) {
			return null;
		}
		return new BigInteger(value);
	 } catch (Exception e ) {
	    	e.printStackTrace();
	    	LOGGER.error("ERRO CONVERSAO INTEGER='"+value+"'");
	    	throw new ConversionError(MessageFormat.format(bundle.getString("is_not_a_valid_integer"), value));    
	    }	
	}

}
