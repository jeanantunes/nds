package br.com.abril.icd.axis.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;


/**
 * Utilitário que encapsula os métodos referentes à semana. 
 * 
 * @author Discover Technology
 *
 */
public class SemanaUtil {

	/**
	 * Dada uma String ano + semana, é retornado o número correspondente à semana.
	 * 
	 * @param anoSemana - String no padrão ano + semana. Ex.: 201321
	 * 
	 * @return Os dígitos referentes à semana, no caso: 21.
	 */
	public static Integer getSemana(Integer anoSemana) {
		
		return getSemana(anoSemana.toString());
	}
	
	/**
	 * Dada uma String ano + semana, é retornado o número correspondente à semana.
	 * 
	 * @param anoSemana - String no padrão ano + semana. Ex.: 201321
	 * 
	 * @return Os dígitos referentes à semana, no caso: 21.
	 */
	public static Integer getSemana(String anoSemana) {

		try {
			
			if (anoSemana == null) {
				
				return null;
			}
			
			return Integer.parseInt(anoSemana.substring(4));

		} catch (NumberFormatException e) {
			
			throw new IllegalArgumentException("Número de ano/semana inválido.");

		} catch (IndexOutOfBoundsException e) {
			
			throw new IllegalArgumentException("Número de ano/semana inválido.");
		}		
	}
	
	/**
	 * Dada uma String ano + semana e uma data, é retornado o ano de referência da mesma.
	 *  
	 * @param anoSemana
	 * 
	 * @return Ano de referência.
	 */
	public static Integer getAno(Integer anoSemana) {
		
		return getAno(anoSemana.toString());
	}
	
	/**
	 * Dada uma String ano + semana e uma data, é retornado o ano de referência da mesma.
	 *  
	 * @param anoSemana
	 * 
	 * @return Ano de referência.
	 */
	public static Integer getAno(String anoSemana) {
		
		if (anoSemana == null) {
			
			return null;
		}

		try {
			
			return Integer.parseInt(anoSemana.substring(0,4));
	
		} catch (NumberFormatException e) {
	
			throw new IllegalArgumentException("Número de ano/semana inválido.");
	
		} 
	}
	
	/**
	 * Obtém o ano e número da semana no ano referente à data desejada
	 *  
	 * @param data - data
	 * @param diaInicioSemana - dia de início da semana (Utilizar as constantes da classe java.util.Calendar)
	 * 
	 * @return Ano e número da semana no ano da data passada por parâmetro
	 */
	public static String obterAnoNumeroSemana(Date data, Integer diaInicioSemana) {
		
	    Integer mes = DateUtil.obterMes(data);
		
		Integer anoBase = DateUtil.obterAno(data);
		
		if (mes == Calendar.JANUARY) {
			
			/* Caso o mes seja janeiro, obtém a data da primeira semana do ano.
			 * Se a data for informada menor que a data de início da semana,
			 * utiliza um ano anterior como base.
			 */ 

			Date dataInicioSemana = obterDataDaSemanaNoAno(1, diaInicioSemana, anoBase);
			
			if (data.before(dataInicioSemana)) {
				
				anoBase--;
			}
			
		} else if (mes == Calendar.DECEMBER) {
		
			/*
			 * Caso o mes seja dezembro, obtém a data da primeira semana do ano seguinte.
			 * Se a data informada for menor que a data de início da semana do ano seguinte,
			 * utiliza o ano da data informada. Senão utiliza o ano seguinte como base.
			 */
			
			anoBase++;
			
			Date dataInicioSemana = obterDataDaSemanaNoAno(1, diaInicioSemana, anoBase);
			
			if (data.before(dataInicioSemana)) {
				
				anoBase--;
			}	
		}
		
		// Obtém o número da semana de acordo com o ano base calculado anteriormente.
		
		Integer semana = obterNumeroSemanaNoAno(diaInicioSemana, data, anoBase);
		
	//	String anoSemana = this.padLeft(anoBase.toString(),"0",4) + this.padLeft(semana.toString(), "0", 2);
		String anoSemana = String.format("%04d",anoBase.intValue())+String.format("%02d",semana.intValue());
		return anoSemana;
	}
	
	
	/**
	 * Obtém o número da semana no ano referente à data desejada
	 *  
	 * @param data - data
	 * @param diaInicioSemana - dia de início da semana (Utilizar as constantes da classe java.util.Calendar)
	 * 
	 * @return Número da semana no ano da data passada por parâmetro
	 */
	
	
	/**
	 * Obtém a data de acordo com o número e dia de ínicio de uma semana.
	 * 
	 * @param numeroSemana - número da semana no ano
	 * @param diaInicioSemana - dia de início da semana (Utilizar as constantes da classe java.util.Calendar)
	 * @param anoBase
	 * @return Data
	 */
	public static Date obterDataDaSemanaNoAno(Integer numeroSemana, Integer diaInicioSemana, Integer anoBase) {
		
		if (numeroSemana == null) {
			
			throw new IllegalArgumentException("Número da semana inválido!");
		}
		
		if (diaInicioSemana == null) {
			
			throw new IllegalArgumentException("Dia de ínicio da semana inválido!");
		}
		
		Integer diaSemanaPrimeiroDia = getDiaSemanaPrimeiroDiaAno(anoBase);
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.clear();
		
		if (anoBase == null) {
		
			anoBase = DateUtil.obterAno(new Date());
		}
		
		if (diaInicioSemana <= diaSemanaPrimeiroDia) {
			
			calendar.setMinimalDaysInFirstWeek(7);
		}
		
		calendar.set(Calendar.YEAR, anoBase);
		
		calendar.set(Calendar.WEEK_OF_YEAR, numeroSemana);
		
		calendar.setFirstDayOfWeek(diaInicioSemana);
		
		Date data = calendar.getTime();
		
		return DateUtil.removerTimestamp(data);
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
	
	public static int obterDiaDaSemana(Calendar data) {
		
		return obterDiaDaSemana(data.getTime());
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
				
				datas.add(DateUtil.removerTimestamp(dataInicial));
			}
			
			dataInicial = DateUtil.adicionarDias(dataInicial, 1);
		}
		
		return datas;
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
	
	private static int obterNumeroSemanaNoAno(Integer diaInicioSemana, Date data, int anoBase) {
		
		if (data == null) {
			
			throw new IllegalArgumentException("Data inválida!");
		}
		
		if (diaInicioSemana == null) {
			
			throw new IllegalArgumentException("Dia de ínicio da semana inválido!");
		}
		
		Integer diaSemanaPrimeiroDia = getDiaSemanaPrimeiroDiaAno(anoBase);
		
		Calendar calendar = Calendar.getInstance();
		
		if (diaInicioSemana <= diaSemanaPrimeiroDia) {
			
			calendar.setMinimalDaysInFirstWeek(7);
		}
		
		calendar.setTime(data);
		
		calendar.setFirstDayOfWeek(diaInicioSemana);
		
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	private static Integer getDiaSemanaPrimeiroDiaAno(Integer anoBase) {
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.clear();
		
		calendar.set(Calendar.YEAR, anoBase);
		
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
}
