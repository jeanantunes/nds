package br.com.abril.nds.util;

public abstract class StringUtil {
	/**
	 * Verifica se a String é valida
	 * @param str
	 * @return <code>true</code> Ser a a string for nula ou vazia
	 */
	public static boolean isEmpty(String str){
		return (str == null) || str.isEmpty();
	}

}
