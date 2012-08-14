package br.com.abril.nds.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Classe utilitária para complexidades matemáticas.
 * 
 * @author Discover Technology
 */
public abstract class MathUtil {
	
	public static final int PADRAO_PRECISAO_DECIMAL = 4;
	
	private static final int TOTAL_CASAS_DECIMAIS = 20;
	
	/**
	 * Efetua o arredondamento de um BigDecimal.
	 * 
	 * @param value - valor
	 * @param scale - precisão de casas decimais
	 * 
	 * @return BigDecimal arredondado
	 */
	public static BigDecimal round(BigDecimal value, Integer scale) {
		
		if (value == null) {
			
			return null;
		}
		
		if (scale != null) {
			
			value = value.setScale(scale, RoundingMode.HALF_EVEN);
		}
		
		return value.round(new MathContext(TOTAL_CASAS_DECIMAIS, RoundingMode.HALF_EVEN));
	}
	
	/**
	 * Efetua a divisão de um BigDecimal.
	 * 
	 * @param dividend - dividendo
	 * @param divisor - divisor
	 * 
	 * @return BigDecimal arredondado
	 */
	public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {

		return dividend.divide(divisor, new MathContext(TOTAL_CASAS_DECIMAIS, RoundingMode.HALF_EVEN));
	}
	
	/**
	 * Retorna um valor padrão caso o valor passado seja nulo.
	 * 
	 * @param value - valor
	 * 
	 * @return Valor ou valor padrão.
	 */
	public static BigDecimal defaultValue(BigDecimal value) {
		
		if (value == null) {
			
			return BigDecimal.ZERO;
		}
		
		return value;
	}

}
