package br.com.abril.nds.converters;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.ResourceBundle;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.ioc.RequestScoped;

@Convert(String.class)
@RequestScoped
public class StringConverter implements Converter<String> {
	
	@Override
	public String convert(String value, Class<? extends String> type, ResourceBundle bundle) {
		if (isNullOrEmpty(value)) {
			return "";
		}
		return value;
	}

}
