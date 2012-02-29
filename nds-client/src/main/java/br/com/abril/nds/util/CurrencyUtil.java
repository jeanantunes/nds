package br.com.abril.nds.util;

import java.text.DecimalFormat;
import java.util.Currency;

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
	 * @param codigoMoeda - código da moeda
	 * 
	 * @return Valor formatado
	 */
	public static String formatarValor(Double valor, String codigoMoeda) {
		
		Currency currency = Currency.getInstance(codigoMoeda);
		
		DecimalFormat decimalFormat = new DecimalFormat(currency.getSymbol() + "#,##0.00");
		
		return decimalFormat.format(valor);
	}
	
}
