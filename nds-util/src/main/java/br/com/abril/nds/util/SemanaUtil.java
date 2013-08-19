package br.com.abril.nds.util;

import java.util.Calendar;
import java.util.Date;

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
	public static Integer get(String anoSemana) {

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
	 * Dada uma String ano + semana e uma data, é retornado uma data com base no ano.
	 * 
	 * @param anoSemana - String no padrão ano + semana. Ex.: 201321
	 *  
	 * @param data - Data base.
	 * 
	 * @return {@link Date}
	 */
	public static Date getDateBase(String anoSemana, Date data) {
		
		if (anoSemana == null) {
			
			return null;
		}

		try {

			Integer ano = getAno(anoSemana);

			Calendar c = Calendar.getInstance();
			c.setTime(data);
			c.set(Calendar.YEAR, ano);
			
			return c.getTime();

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
}
