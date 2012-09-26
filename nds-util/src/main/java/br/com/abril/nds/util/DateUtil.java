package br.com.abril.nds.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

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
			
				long diferencaMilisegundos = dataFinal.getTime() - dataInicial.getTime();
				
				quantidadeDias = diferencaMilisegundos / (24 * 60 * 60 * 1000);
			}
		}
		
		return quantidadeDias;
	}
	
	/**
	 * Obtém a número da semana no ano da data desejada utilizando o
	 * código do dia de ínicio da semana.
	 *  
	 * @param data - data
	 * @param diaInicioSemana - dia de início da semana (Utilizar as constantes da classe java.util.Calendar)
	 * 
	 * @return Número da semana no ano da data passada por parâmetro
	 */
	public static int obterNumeroSemanaNoAno(Date data, Integer diaInicioSemana) {
				
		if (data == null) {
			
			throw new IllegalArgumentException("Data inválida!");
		}
		
		if (diaInicioSemana == null) {
			
			throw new IllegalArgumentException("Dia de ínicio da semana inválido!");
		}
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(data);
		
		if (diaInicioSemana != null) {
		
			calendar.setFirstDayOfWeek(diaInicioSemana);
		}
		
		return calendar.get(Calendar.WEEK_OF_YEAR);
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
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(data);

		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	/**
	 * Obtém a data de acordo com o número e dia de ínicio de uma semana.
	 * 
	 * @param numeroSemana - número da semana no ano
	 * @param diaInicioSemana - dia de início da semana (Utilizar as constantes da classe java.util.Calendar)
	 * @param dataBase TODO
	 * @return Data
	 */
	public static Date obterDataDaSemanaNoAno(Integer numeroSemana, Integer diaInicioSemana, Date dataBase) {
		
		if (numeroSemana == null) {
			
			throw new IllegalArgumentException("Número da semana inválido!");
		}
		
		if (diaInicioSemana == null) {
			
			throw new IllegalArgumentException("Dia de ínicio da semana inválido!");
		}
		
		Calendar calendar = Calendar.getInstance();
		if (dataBase != null) {
			calendar.setTime(dataBase);
		}
		
		int year = calendar.get(Calendar.YEAR);
		
		calendar.clear();
		
		calendar.set(Calendar.YEAR, year);
		
		calendar.set(Calendar.WEEK_OF_YEAR, numeroSemana);
		
		calendar.setFirstDayOfWeek(diaInicioSemana);
		
		Date data = calendar.getTime();
		
		return removerTimestamp(data);
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
	}
	
	/**
	 * Obtém um período filtrado de acordo com os dias da semana desejados. 
	 * 
	 * @param dataInicial - data inicial do período
	 * @param dataFinal - data final do período
	 * @param listaCodigosDiasSemana - lista de códigos com os dias da semana
	 * 
	 * @return Período de datas filtrado pelos dias da semana
	 */
	public static TreeSet<Date> obterPeriodoDeAcordoComDiasDaSemana(Date dataInicial, 
																	Date dataFinal,
																	Collection<Integer> listaCodigosDiasSemana) {
		
		if (dataInicial == null) {
			
			throw new IllegalArgumentException("Data inicial inválida!");
		}
		
		if (dataFinal == null) {
			
			throw new IllegalArgumentException("Data final inválida!");
		}
		
		if (listaCodigosDiasSemana == null || listaCodigosDiasSemana.isEmpty()) {
			
			throw new IllegalArgumentException("Códigos de dias da semana inválidos!");
		}
		
		TreeSet<Date> datas = new TreeSet<Date>();
		
		while (dataInicial.before(dataFinal) || dataInicial.equals(dataFinal)) {
			
			int diaDaSemana = obterDiaDaSemana(dataInicial);
			
			if (listaCodigosDiasSemana.contains(diaDaSemana)) {
				
				datas.add(dataInicial);
			}
			
			dataInicial = DateUtil.adicionarDias(dataInicial, 1);
		}
		
		return datas;
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
	
	public static String obterDiaSemana(int codigoDiaSemana){
		
		switch(codigoDiaSemana){
			case Calendar.SUNDAY:
				return "Domingo";
			case Calendar.MONDAY:
				return "Segunda-feira";
			case Calendar.TUESDAY:
				return "Terça-feira";
			case Calendar.WEDNESDAY:
				return "Quarta-feira";
			case Calendar.THURSDAY:
				return "Quinta-feira";
			case Calendar.FRIDAY:
				return "Sexta-feira";
			case Calendar.SATURDAY:
				return "Sabado-feira";
			default:
				return "Segunda-feira";
		}
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
