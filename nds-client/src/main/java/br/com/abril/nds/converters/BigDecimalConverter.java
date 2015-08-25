package br.com.abril.nds.converters;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.converter.ConversionError;
import br.com.caelum.vraptor.ioc.ApplicationScoped;

/**
 * VRaptor's BigDecimal converter.
 *
 * @author Cecilia Fernandes
 */
@Convert(BigDecimal.class)
@ApplicationScoped
public class BigDecimalConverter implements Converter<BigDecimal>{

	 private static final Logger LOGGER = LoggerFactory.getLogger(BigDecimalConverter.class);
	public BigDecimal convert(String value, Class<? extends BigDecimal> type, ResourceBundle bundle) {
		LOGGER.error("BIGDECIMAL CONVERTER"+value);
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

		
		try {
			//return (BigDecimal) getNumberFormat().parse(value);
			LOGGER.error(value.replaceAll("\\.","").replaceAll(",","."));
			return new BigDecimal(value.replaceAll("\\.","").replaceAll(",","."));
		}   catch (Exception e ) {
	    	e.printStackTrace();
	    	LOGGER.error("ERRO DE CONVERSAO BIGDECIMAL='"+value+"' "+value.replaceAll(".","").replaceAll(",","."));
	    	throw new ConversionError(MessageFormat.format(bundle.getString("is_not_a_valid_number"), value));    
	    }
	}
		
		protected NumberFormat getNumberFormat() {
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setGroupingSeparator('.');
			symbols.setDecimalSeparator(',');
			String pattern = "#.##0,0#";
			DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
			return decimalFormat;
		}

	
}
