package br.com.abril.nds.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
<<<<<<< HEAD
=======
import java.util.GregorianCalendar;
import java.util.TreeSet;
>>>>>>> fase2

import org.apache.commons.lang.time.DateUtils;

public class DateUtil {
    
	public static final String PADRAO_HORA_MINUTO = "HH:mm";

	public static boolean isValidDate(String valor, String pattern) {

		if (valor == null || pattern == null){
			
			return false;
		}
		
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
		
		if (date == null){
			
			return false;
		}
		
		return isValidDate(date, Constantes.DATE_PATTERN_PT_BR);
	}
	
	public static String formatarData(Date data, String formato) {
		
		if (data == null || formato == null){
			
			return "";
		}
		
		return new SimpleDateFormat(formato).format(data);
	}
	
    /**
     * Formata a data com a informação de hora e minutos
     * 
     * @param data
     *            data para formatação
     * @return informação de horas e minutos da data no padrão
     *         {@link DateUtil#PADRAO_HORA_MINUTO}
     */
	public static String formatarHoraMinuto(Date data) {
	    return formatarData(data, PADRAO_HORA_MINUTO);
	}
	
	public static String formatarDataPTBR(Date data) {
		
		if (data == null){
			
			return "";
		}
		
		return formatarData(data, Constantes.DATE_PATTERN_PT_BR);
	}

	/**
	 * Normaliza uma data, para comparações, zerando os valores de hora (hora,
	 * minuto, segundo e milissendo).
	 * 
	 * @param dt
	 * 
	 * @return
	 */
	public static Date normalizarDataSemHora(Date dt) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
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
		return removerTimestamp(cal.getTime());
	}

	public static Calendar adicionarDias(Calendar data, int numDias) {
		
		return toCalendar(adicionarDias(data.getTime(), numDias));
	}
	
	public static Calendar toCalendar(Date data) {
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(data);
		
		return calendar;
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
	 * Subtrai o número de meses da data
	 * 
	 * @param data - data para subtração de meses
	 * @param numMes - número de meses para subtrair
	 * 
	 * @return nova data com o número de meses subtraidos
	 */
	public static Date subtrairMeses(Date data, int numMes) {
		if (data == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.MONTH, -numMes);
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

		if(data == null)
			return null;
		
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
			
				long diferencaMilisegundos = dataFinal.getTime() - dataInicial.getTime();
				
				quantidadeDias = diferencaMilisegundos / (24 * 60 * 60 * 1000);
			}
		}
		
		return quantidadeDias;
	}
	
	public static int obterMes(Date data) {
		
<<<<<<< HEAD
		Calendar calendar = toCalendar(data);
=======
		int semana = calendar.get(Calendar.WEEK_OF_YEAR);
		
		return Integer.parseInt(calendar.get(Calendar.YEAR) +""+ ((semana < 10)?"0"+semana:semana));
	}
	
	/**
	 * Obtém a número da semana no ano da data desejada.
	 * 
	 * Será utilizado o padrão de acordo com o Locale do sistema.
	 *  
	 * @param data - data
	 * 
	 * @return Número da semana no ano da data passada por parâmetro
	 */
	public static int obterNumeroSemanaNoAno(Date data) {
		
		if (data == null) {
			
			throw new IllegalArgumentException("Data inválida!");
		}
>>>>>>> fase2
		
		return calendar.get(Calendar.MONTH);
	}
	
	public static int obterAno(Calendar calendar) {
		
		return calendar.get(Calendar.YEAR);
	}
	
	public static int obterAno(Date data) {
		
		Calendar calendar = toCalendar(data);
		
<<<<<<< HEAD
		return obterAno(calendar);
=======
		calendar.setTime(data);
		
		int year = calendar.get(Calendar.YEAR);
		
		int month = calendar.get(Calendar.MONTH);
		
		if (month == Calendar.JANUARY) {
			
			if (numeroSemana > QTD_MAXIMA_SEMANAS_POR_MES) {
				
				year--;
			}
		}
		
		return year;
	}

	private static Calendar getCalendarioDistribuidor(Integer diaInicioSemana, Date data) {
		
		if (data == null) {
			
			throw new IllegalArgumentException("Data inválida!");
		}
		
		if (diaInicioSemana == null) {
			
			throw new IllegalArgumentException("Dia de ínicio da semana inválido!");
		}
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.setMinimalDaysInFirstWeek(7);
		
		calendar.setTime(data);
		
		if (diaInicioSemana != null) {
		
			calendar.setFirstDayOfWeek(diaInicioSemana);
		}
		
		return calendar;
	}
	
	
	public static Date obterDtInicioSemanaPorNumeroSemanaAno(Integer inicioSemana,Integer numeroSemana, Integer ano){
		GregorianCalendar dataInicio = new GregorianCalendar();
		dataInicio.set(Calendar.YEAR, ano);  
		dataInicio.setFirstDayOfWeek(inicioSemana);  
		dataInicio.set(Calendar.WEEK_OF_YEAR, numeroSemana);  
		
	
		Date retorno = dataInicio.getTime();
		
		return retorno;
	}
	
	/**
	 * Retorna o código do dia da semana de uma determinada data.
	 * 
	 * @param data - data
	 * 
	 * @return Código do dia da semana (verificar constantes da classe java.util.Calendar)
	 */
	public static int obterDiaDaSemana(Date data) {
		
		if (data == null) {
			
			throw new IllegalArgumentException("Data inválida!");
		}
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(data);
		
		return calendar.get(Calendar.DAY_OF_WEEK);
>>>>>>> fase2
	}
	
	/**
	 * Retorna o dia do mês de uma determinada data.
	 * 
	 * @param data - data
	 * 
	 * @return Dia do mês (verificar constantes da classe java.util.Calendar)
	 */
	public static int obterDiaDoMes(Date data) {
		
		if (data == null) {
			
			throw new IllegalArgumentException("Data inválida!");
		}
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(data);
		
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Valida se a data está entre o período informado.
	 * 
	 * @param dataComparacao - data para comparação
	 * @param dataInicial - data inicial do período
	 * @param dataFinal - data final do período
	 * 
	 * @return flag indicando se a data está entre o período
	 */
	public static boolean validarDataEntrePeriodo(Date dataComparacao,
												  Date dataInicial,
												  Date dataFinal) {
		
		boolean periodoInvalido = isDataInicialMaiorDataFinal(dataInicial, dataFinal);
		
		if (!periodoInvalido && dataComparacao != null) {
		
			if (dataComparacao.compareTo(dataInicial) >= 0
					&& dataComparacao.compareTo(dataFinal) <= 0) {
				
				return true;
			}
		}
		
		return false;
	}
	
	public static String obterDecricaoMes(int codigoMes){
		
		switch(codigoMes){
		
			case Calendar.JANUARY:
				return "Janeiro";
				
			case Calendar.FEBRUARY:
				return "Fevereiro";
			
			case Calendar.MARCH:
				return "Março";
				
			case Calendar.APRIL:
				return "Abril";
				
			case Calendar.MAY:
				return "Maio";
				
			case Calendar.JUNE:
				return "Junho";
				
			case Calendar.JULY:
				return "Julho";

			case Calendar.AUGUST:
				return "Agosto";

			case Calendar.SEPTEMBER:
				return "Setembro";

			case Calendar.OCTOBER:
				return "Outubro";

			case Calendar.NOVEMBER:
				return "Novembro";

			case Calendar.DECEMBER:
				return "Dezembro";

				
			default:
				return "Dezembro";
		}
	}
	
	
	
	
	
}
