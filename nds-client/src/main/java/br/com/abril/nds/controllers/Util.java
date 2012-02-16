package br.com.abril.nds.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
}
