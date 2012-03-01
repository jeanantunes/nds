package br.com.abril.nds.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Currency;

import br.com.caelum.vraptor.core.Localization;

/**
 * Classe utilitária para moedas.
 * 
 * @author Discover Technology
 *
 */
public abstract class CurrencyUtil {

	/**
	 * Formata um valor de moeda com ou sem símbolo monetário.
	 * 
	 * @param valor - valor
	 * @param localization - localization do VRaptor (necessário para símbolo monetário)
	 * 
	 * @return Valor formatado
	 */
	public static String formatarValor(Number valor, Localization localization) {
		
		if (valor == null) {
			
			return null;
		}
		
		DecimalFormat decimalFormat = null;
		
		if (localization != null) {
		
			Currency currency = Currency.getInstance(localization.getLocale());
		
			decimalFormat = new DecimalFormat(currency.getSymbol() + " #,##0.00");
			
		} else {
			
			decimalFormat = new DecimalFormat("#,##0.00");
		}
		
		return decimalFormat.format(valor);
	}
	
	/**
	 * Formata um valor de moeda sem símbolo monetário.
	 * 
	 * @param valor - valor
	 * 
	 * @return Valor formatado
	 */
	public static String formatarValor(Number valor) {

		return formatarValor(valor, null);
	}
	
}
