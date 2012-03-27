package br.com.abril.nds.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

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

	public static boolean isDataInicialMaiorDataFinal(Date dataInicial, Date dataFinal) {
		
		if (dataInicial != null && dataFinal != null) {
			
			return dataInicial.compareTo(dataFinal) > 0;
		}
		
		return false;
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
	
	/**
	 * Subtrai o número de dias da data
	 * 
	 * @param data - data para subtração de dias
	 * @param numDias - número de dias para subtrair
	 * 
	 * @return nova data com o número de dias subtratidos
	 */
	public static Date subtrairDias(Date data, int numDias) {
		if (data == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_MONTH, -numDias);
		return cal.getTime();
	}
	
	/**
	 * Método que verifica se a data informada é sábado ou domingo
	 * 
	 * @param cal - data para comparação
	 * 
	 * @return variável indicando se a data é sábado ou domingo
	 */
	public static boolean isSabadoDomingo(Calendar cal) {
		return (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
				|| (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
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
	
	public static boolean isHoje(Date data) {
		if (data == null) {
			return false;
		}
		return DateUtils.isSameDay(new Date(), data);
	}
	
	public static long obterDiferencaDias(Date dataInicial, Date dataFinal) {
		
		long quantidadeDias = 0;
		
		if (dataInicial != null && dataFinal != null) {
			
			if (dataInicial.compareTo(dataFinal) != 1) {
			
				long diferencaMilisegundos = dataInicial.getTime() - dataFinal.getTime();
				
				quantidadeDias = diferencaMilisegundos / (24 * 60 * 60 * 1000);
			}
		}
		
		return quantidadeDias;
	}
	
	
}
