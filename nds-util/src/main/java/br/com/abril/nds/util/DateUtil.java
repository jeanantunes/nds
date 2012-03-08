package br.com.abril.nds.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static boolean isValidDate(String valor, String pattern) {

		try {
			DateFormat f = new SimpleDateFormat(pattern);
			
			f.setLenient(false);
			
			f.parse(valor);
			
		} catch (ParseException n) {
			
			return false;
		}
		
		return true;
	}
	
	public static boolean isValidDatePTBR(String date) {
		
		return isValidDate(date, Constantes.DATE_PATTERN_PT_BR);
	}
	
	public static String formatarData(Date data, String formato) {
		
		return new SimpleDateFormat(formato).format(data);
	}
	
	public static String formatarDataPTBR(Date data) {
		
		return formatarData(data, Constantes.DATE_PATTERN_PT_BR);
	}
	
	public static boolean isDataFinalMaiorDataInicial(Date dataInicial, Date dataFinal) {
		return dataInicial.compareTo(dataFinal) > 0;
	}

	/**
	 * Avança o número de dias a data
	 * 
	 * @param data
	 *            data para adição de dias
	 * @param numDias
	 *            número de dias para adicionar
	 * @return data com o número de dias adicionados
	 */
	public static Date adicionarDias(Date data, int numDias) {
		if (data == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_MONTH, numDias);
		return cal.getTime();
	}
	
	public static Date parseData(String data, String formato) {
		
		try {
			
			DateFormat f = new SimpleDateFormat(formato);
			
			f.setLenient(false);
			
			Date parsedData = f.parse(data);
			
			return parsedData;
		
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date parseDataPTBR(String data) {
		
		return parseData(data, Constantes.DATE_PATTERN_PT_BR);
	}
	
	/**
	 * Remove a informa??o de timestamp da data
	 * 
	 * @param data
	 *            data para remo??o do timestamp
	 * @return data sem informa??o de timestamp
	 */
	public static Date removerTimestamp(Date data) {
		if (data == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	
}
