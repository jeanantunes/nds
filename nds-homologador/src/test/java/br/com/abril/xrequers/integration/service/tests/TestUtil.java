package br.com.abril.xrequers.integration.service.tests;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.mockito.ArgumentMatcher;

public class TestUtil {

	public static Date criarData(int dia, int mes, int ano) {
		Calendar data = criarCalendar(dia, mes, ano, 0, 0, 0);
		return data.getTime();
	}

	private static Calendar criarCalendar(int dia, int mes, int ano, int hora,
			int minuto, int segundo) {
		Calendar data = Calendar.getInstance();
		data.set(Calendar.DAY_OF_MONTH, dia);
		data.set(Calendar.MONTH, mes);
		data.set(Calendar.YEAR, ano);
		data.set(Calendar.HOUR_OF_DAY, hora);
		data.set(Calendar.MINUTE, minuto);
		data.set(Calendar.SECOND, segundo);
		data.clear(Calendar.MILLISECOND);
		return data;
	}

	/**
	 * Matcher para representar (de acordo com o construtor usado) data que esta
	 * dentro da lista de datas possíveis ou data que esta dentro do período de
	 * dataInicial e dataFinal;
	 */
	static class IsInDateRange extends ArgumentMatcher<Date> {

		private List<Date> datasPossiveis;

		private Date dataInicial;
		private Date dataFinal;

		IsInDateRange(List<Date> datasPossiveis) {
			this.datasPossiveis = datasPossiveis;
		}

		IsInDateRange(Date dataInicial, Date dataFinal) {
			this.dataInicial = dataInicial;
			this.dataFinal = dataFinal;
		}

		public boolean matches(Object data) {

			if (datasPossiveis != null && !datasPossiveis.isEmpty()) {

				for (Date d : datasPossiveis) {
					if (d.getTime() == ((Date) data).getTime()) {
						return true;
					}
				}

				return false;

			} else {

				boolean isDataNoPeriodo = ((Date) data).getTime() >= dataInicial
						.getTime()
						&& ((Date) data).getTime() <= dataFinal.getTime();

				return isDataNoPeriodo;

			}

		}

	}
	
	/**
	 * Matcher para reprensetar data que equivale a dia util 
	 * (todos os dias exceto Sabado e Domingo)!
	 */
	static class IsWeek extends ArgumentMatcher<Date> {
	
		public boolean matches(Object data) {
			
			Calendar c = Calendar.getInstance();
			c.setTime((Date)data);
			
			return (Calendar.SUNDAY != c.get(Calendar.DAY_OF_WEEK) && 
				Calendar.SATURDAY != c.get(Calendar.DAY_OF_WEEK)); 

		}

	}
	

}
