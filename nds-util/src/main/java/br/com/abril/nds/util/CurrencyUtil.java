package br.com.abril.nds.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import br.com.caelum.stella.inwords.FormatoDeReal;
import br.com.caelum.stella.inwords.NumericToWordsConverter;

/**
 * Classe utilitária para moedas.
 * 
 * @author Discover Technology
 *
 */
public abstract class CurrencyUtil {

	/**
	 * Formata um valor de moeda sem símbolo monetário.
	 * 
	 * @param valor - valor
	 * 
	 * @return Valor formatado
	 */
	public static String formatarValor(Number valor) {
		
		if (valor == null) {
			
			return null;
		}

		return new DecimalFormat("#,##0.00").format(valor);
	}
	
	/**
	 * Formata um valor de moeda com símbolo monetário.
	 * 
	 * @param valor - valor
	 * 
	 * @return Valor formatado
	 */
	public static String formatarValorComSimbolo(Number valor) {
		
		String valorFormatado = formatarValor(valor);
		
		if (valorFormatado != null) {
			
			valorFormatado = "R$ " + valorFormatado; 
		}
		
		return valorFormatado;
	}
	
	/**
	 * Converte uma String de moeda com ou sem símbolo monetário.
	 * 
	 * @param valor - valor
	 * @param locale - locale recebido do cliente (necessário para símbolo monetário)
	 * 
	 * @return valor convertido
	 */
	public static BigDecimal converterValor(String valor, Locale locale) {
		
		if (valor == null) {
			
			return null;
		}
		
		DecimalFormat decimalFormat = null;
		
		if (locale != null) {
		
			Currency currency = Currency.getInstance(locale);
		
			decimalFormat = new DecimalFormat(currency.getSymbol() + " #,##0.00");
			
		} else {
			
			decimalFormat = new DecimalFormat("#,##0.00");
		}
		
		try {
		
			return new BigDecimal(decimalFormat.parse(valor).toString());
		
		} catch (ParseException e) {
			
			return null;
		}
	}
	
	/**
	 * Converte uma String de moeda sem símbolo monetário.
	 * 
	 * @param valor - valor
	 * 
	 * @return Valor formatado
	 */
	public static BigDecimal converterValor(String valor) {

		return converterValor(valor, null);
	}
	
	/**
	 * Converte um valor em formato extenso.
	 * 
	 * @param valor - valor
	 * 
	 * @return Valor formatado
	 */
	public static String valorExtenso(BigDecimal valor) {
		
		NumericToWordsConverter converter = new NumericToWordsConverter(new FormatoDeReal());  
		
		return converter.toWords(valor.doubleValue());
	}
	
}
