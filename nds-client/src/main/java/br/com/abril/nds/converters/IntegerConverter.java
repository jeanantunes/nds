package br.com.abril.nds.converters;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.ResourceBundle;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.ioc.RequestScoped;

@Convert(Integer.class)
@RequestScoped
public class IntegerConverter implements Converter<Integer> {
	
	@Override
	public Integer convert(String value, Class<? extends Integer> type, ResourceBundle bundle) {
		if (isNullOrEmpty(value)) {
			return null;
		}
		return Integer.parseInt(value);
		
	}

}
