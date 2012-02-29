package br.com.abril.nds.util;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Classe utilitária para moedas.
 * 
 * @author Discover Technology
 *
 */
public abstract class CurrencyUtil {

	/**
	 * Formata um valor de moeda de acordo com seu código.
	 * 
	 * @param valor - valor
	 * @param locale - locale
	 * 
	 * @return Valor formatado
	 */
	public static String formatarValor(Double valor, Locale locale) {
		
		if (valor == null) {
			
			return null;
		}
		
		Currency currency = Currency.getInstance(locale);
		
		DecimalFormat decimalFormat = new DecimalFormat(currency.getSymbol() + "#,##0.00");
		
		return decimalFormat.format(valor);
	}
	
}
