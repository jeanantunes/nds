package br.com.abril.nds.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Currency;
import java.util.List;
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

	public static final String SIMBOLO_BRL = "R$";
	
	
	public static DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt","BR"));
	
	    /**
     * Formata um valor de moeda sem símbolo monetário.
     * 
     * @param valor - valor
     * 
     * @return Valor formatado
     */
	public static String formatarValor(Number valor) {
		
		if (valor == null) {
			
			return "0,00";
		}

		return new DecimalFormat("#,##0.00",symbols).format(valor);
	}
	
	    /**
     * Formata um valor de moeda sem símbolo monetário.
     * 
     * @param valor - valor
     * 
     * @return Valor formatado
     */
	public static String formatarValorQuatroCasas(Number valor) {
		
		if (valor == null) {
			
			return null;
		}

		return new DecimalFormat("#,##0.0000",symbols).format(valor);
	}

	    /**
     * Formata um valor de moeda sem símbolo monetário e sem a parte fracionária
     * (truncado).
     * 
     * @param valor - valor
     * 
     * @return Valor formatado
     */
	public static String formatarValorTruncado(Number valor) {
		
		if (valor == null) {
			
			return null;
		}

		return new DecimalFormat("#,##0",symbols).format(valor);
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
			
			valorFormatado = SIMBOLO_BRL + " " + valorFormatado; 
		}
		
		return valorFormatado;
	}
	
	    /**
     * Converte uma String de moeda com ou sem símbolo monetário.
     * 
     * @param valor - valor
     * @param locale - locale recebido do cliente (necessário para símbolo
     *            monetário)
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
			
			decimalFormat = new DecimalFormat("#,##0.00",symbols);
		}
		
		try {
		
			return new BigDecimal(decimalFormat.parse(valor).toString());
		
		} catch (ParseException e) {
			
			return null;
		}
	}
	
	
	    /**
     * Obtém o BigDecimal de uma String no formato monetario brasileiro
     * 
     * @param numero - valor em String Ex: 1.000,00
     * @return BigDecimal 1000.00
     */
	public static BigDecimal getBigDecimal(String numero) {
		return new BigDecimal(numero.replace(".", "").replace(",", "."));
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
	
	public static String convertValor(String valor, int decimalScale) {
		
		if (valor == null) {

			return "0,00";
		}
		
		BigDecimal value = new BigDecimal(valor).setScale(decimalScale, BigDecimal.ROUND_HALF_EVEN);
		
		return formatarValor(value);
	}
	
	
	/**
	 * Converte uma string em formato internacional para uma string em formato nacional
	 * 
	 *  "1,000.00" to "1.000,00"
	 * 
	 * @param valor 
	 * @return
	 */
	public static String convertValor(String valor) {
		
		if(valor == null || valor.isEmpty())
			return "0,00";
		
		if(valor.indexOf(",") < valor.indexOf(".")) {
			valor = valor.replace(",", "");
		} 
		
		valor = valor.replace(".", ",");
		
		return valor;
	}
	
	
	/**
	 * Converte uma string em formato nacional para uma string em formato internacional 
	 * 
	 *  "1.000,00" to "1000.00"
	 * 
	 * @param valor 
	 * @return
	 */
	public static String convertValorInternacional(String valor) {
		
		if(valor == null || valor.isEmpty())
			return "0.00";
		
		if(valor.indexOf(".") < valor.indexOf(",")) {
			valor = valor.replace(".", "");
		} 
		
		valor = valor.replace(",", ".");
		
		return valor;
	}
	
	public static String pontuarNaCasaDoMilhar(Number valor) {
		
		String totalFormatadoComVirgula = CurrencyUtil.formatarValor(valor);
		int indexOf = totalFormatadoComVirgula.indexOf(",");

		return totalFormatadoComVirgula.substring(0, indexOf);
	}
	
	/**
	 * Metodo padrao para arredondamentos com 2 casas
	 * 
	 * @param valor
	 * @return
	 */
	public static BigDecimal arredondarValorParaDuasCasas(final BigDecimal valor) {

		BigDecimal valorArredondado = valor.setScale(2, RoundingMode.HALF_EVEN);

		return valorArredondado;
	}

	/**
	 * Metodo padrao para arredondamentos com 4 casas
	 * 
	 * @param valor
	 * @return
	 */
	public static BigDecimal arredondarValorParaQuatroCasas(final BigDecimal valor) {

		BigDecimal valorArredondado = valor.setScale(4, RoundingMode.HALF_EVEN);

		return valorArredondado;
	}
	
	/**
	 * Metodo padrao calculo de valores com 4 casas
	 * 
	 * @param valor
	 * @return
	 */
	public static BigDecimal calculoValores(final BigDecimal precoVenda, final BigDecimal desconto, final BigDecimal reparte) {

		BigDecimal precodesconto  = (precoVenda.multiply(desconto).divide(BigDecimal.valueOf(100))).multiply(reparte); 
		
		BigDecimal valor = new BigDecimal(String.valueOf(precodesconto)).setScale(4, BigDecimal.ROUND_FLOOR);
		
		return valor;
	}
	
	/**
	 * Metodo padrao calculo de valores com 4 casas
	 * 
	 * @param valor
	 * @return
	 */
	public static BigDecimal calculoValorTotal(final List<PrecoReparte> precosRepartes) {

		BigDecimal total = BigDecimal.ZERO;
		 
		for (PrecoReparte precoReparte : precosRepartes) {
			total = total.add(precoReparte.getPrecoDesconto()).setScale(4, BigDecimal.ROUND_FLOOR).multiply(precoReparte.getQuantidade()).setScale(2, BigDecimal.ROUND_FLOOR);
		}
		
		return total;
	}
}