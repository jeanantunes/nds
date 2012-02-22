package br.com.abril.nds.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Util {

	public static boolean isValidNumber(String valor){
		try {
			Long.parseLong(valor);
		} catch (NumberFormatException n){
			return false;
		}
		
		return true;
	}
	
	public static boolean isValidDate(String valor, String pattern){
		if (pattern == null || pattern.trim().isEmpty()){
			pattern = "dd/MM/yyyy";
		}
		try {
			DateFormat f = new SimpleDateFormat(pattern);
			f.setLenient(false);
			f.parse(valor);
		} catch (ParseException n){
			return false;
		}
		
		return true;
	}
	
	public static String formatarData(Date data, String formato) {
		return new SimpleDateFormat(formato).format(data);
	}
}
