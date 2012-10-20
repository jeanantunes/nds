package br.com.abril.nds.converters;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.converter.ConversionError;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.ioc.RequestScoped;

@Convert(Date.class)
@RequestScoped
public class IntegerConverter implements Converter<Date> {

	private final Localization localization;

	public IntegerConverter(Localization localization) {
		this.localization = localization;
	}

	@Override
	public Date convert(String value, Class<? extends Date> type,
			ResourceBundle bundle) {
		if (isNullOrEmpty(value)) {
			return null;
		}

		Locale locale = localization.getLocale();
		if (locale == null) {
			locale = Locale.getDefault();
		}

		DateFormat df = DateFormat
				.getDateInstance(DateFormat.SHORT, locale);
		df.setLenient(false);
		
		try {
			return df.parse(value);
		} catch (ParseException e) {
			throw new ConversionError(MessageFormat.format(
					bundle.getString("is_not_a_valid_date"), value));
		}
	}

}
