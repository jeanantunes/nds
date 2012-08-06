package br.com.abril.nds.client.util;

import java.math.BigInteger;

public class BigIntegerUtil {
	
	
	/**
	 * Soma o valores desprezando nulos passado como paramentro.
	 * @param valores
	 * @return Soma ou ZERO se todos o valores forem nulos.
	 */
	public static BigInteger soma(BigInteger... valores){
		BigInteger soma = BigInteger.ZERO;
		for (BigInteger valor : valores) {
			if(valor != null && !valor.equals(BigInteger.ZERO)){
				soma = soma.add(valor);
			}
		}		
		return soma;
	}

}
