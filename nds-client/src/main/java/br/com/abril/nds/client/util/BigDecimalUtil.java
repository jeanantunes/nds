package br.com.abril.nds.client.util;

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

}
