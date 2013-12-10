package br.com.abril.nds.util;

import java.text.Normalizer;

public abstract class StringUtil {
	/**
	 * Verifica se a String é valida
	 * @param str
	 * @return <code>true</code> Ser a a string for nula ou vazia
	 */
	public static boolean isEmpty(String str){
		return (str == null) || str.isEmpty();
	}

	public static String limparString(String text){
		if(text == null)
			return null;
		
		if(text == "")
			return "";
					
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        return text.replaceAll("[^\\p{ASCII}]", "");
	}
}
