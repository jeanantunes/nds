package br.com.abril.nds.util;

import java.math.BigDecimal;
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
	
	public static boolean isMenorQueZero(BigInteger valor) {
		
		if(valor!=null && valor.compareTo(BigInteger.ZERO)<0) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isMaiorQueZero(BigInteger valor) {
		
		if(valor!=null && valor.compareTo(BigInteger.ZERO)>0) {
			return true;
		}
		
		return false;
	}

	/**
	 * Compara os valores, considerando null na comparação
	 * 
	 * @param v1
	 * @param v2
	 * @return int
	 */
	public static int compareToTryNull(BigInteger v1, BigInteger v2){
		
		if ((v1 == null) && (v2!=null)){
			
			return -1;
		}
		
        if ((v1 != null) && (v2==null)){
			
			return 1;
		}
		
        if (v1 == null && v2 == null){
        	
        	return 0;
        }
        
		return v1.compareTo(v2);
	}
	
	/**
	 * Converte Integer para BigInteger
	 * 
	 * @param v
	 * @return BigInteger
	 */
	public static BigInteger valueOfInteger(Integer v){
		
		if (v == null){
			
			return null;
		}
		
		return BigInteger.valueOf(v.longValue());
	}
	
	/**
	 * Converte BigInteger para BigDecimal
	 *  
	 * @param v
	 * @return BigDecimal
	 */
	public static BigDecimal toBigDecimal(BigInteger v){
		
        if (v == null){
			
			return null;
		}

		return BigDecimal.valueOf(v.longValue());
	}
}
