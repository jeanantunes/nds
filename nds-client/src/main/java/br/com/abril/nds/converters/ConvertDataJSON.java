package br.com.abril.nds.converters;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.ioc.Component;

import com.thoughtworks.xstream.converters.SingleValueConverter;

@Component
public class ConvertDataJSON implements SingleValueConverter {

	private final Localization localization;

	public ConvertDataJSON(Localization localization) {
		super();
		this.localization = localization;
	}

	@Override
	public String toString(Object value) {

		Locale locale = localization.getLocale();
		if (locale == null) {
			locale = Locale.getDefault();
		}

		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
		df.setLenient(false);

		return df.format((Date) value);

	}

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class clazz) {
		return Date.class.isAssignableFrom(clazz);
	}

	@Override
	public Object fromString(String arg0) {
		return arg0;
	}

}
