package br.com.abril.nds.util;

import java.math.BigDecimal;

public class BigDecimalUtil {
	
	
	/**
	 * Soma o valores desprezando nulos passado como paramentro.
	 * @param valores
	 * @return Soma ou ZERO se todos o valores forem nulos.
	 */
	public static BigDecimal soma(BigDecimal... valores){
		BigDecimal soma = BigDecimal.ZERO;
		for (BigDecimal valor : valores) {
			if(valor != null && !valor.equals(BigDecimal.ZERO)){
				soma = soma.add(valor);
			}
		}		
		return soma;
	}
	
	public static boolean isMaiorQueZero(BigDecimal valor) {
		
		if(valor!=null && valor.compareTo(BigDecimal.ZERO)>0) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isMenorQueZero(BigDecimal valor) {
		
		if(valor!=null && valor.compareTo(BigDecimal.ZERO)<0) {
			return true;
		}
		
		return false;
	}
	
	public static boolean eq(BigDecimal value1, BigDecimal value2) {
		
		return value1.compareTo(value2) == 0;
	}
	
	public static boolean neq(BigDecimal value1, BigDecimal value2) {
		
		return !eq(value1, value2);
	}
	
	/**
	 * Caso o valor passado for null,
	 * retorna o BigDecimal ZERO.
	 * 
	 * @param valor
	 * 
	 * @return BigDecimal
	 */
	public static BigDecimal obterValorNaoNulo(BigDecimal valor) {
		
		if(valor == null) {
			return BigDecimal.ZERO;
		}
		
		return valor;
		
	}

}
