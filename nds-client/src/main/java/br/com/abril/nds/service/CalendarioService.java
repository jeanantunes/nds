package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

/**
 * Interface que define serviços referentes a 
 * funcionalidades de calendário.
 * 
 * @author Discover Technology
 */
public interface CalendarioService {
	
	/**
	 * Adiciona dias úteis a uma data.
	 * 
	 * @param data - data a ser adicionada
	 * @param numDias - número de dias
	 * 
	 * @return nova data calculada
 	 */
	Date adicionarDiasUteis(Date data, int numDias);
	
	/**
	 * Subtrai dias úteis a uma data.
	 * 
	 * @param data - data a ser subtraída
	 * @param numDias - número de dias
	 * 
	 * @return nova data calculada
 	 */
	Date subtrairDiasUteis(Date data, int numDias);
	
	/**
	 * Adiciona dias úteis a uma data.
	 * 
	 * @param data - data a ser adicionada
	 * @param numDias - número de dias
	 * @param diasSemana - dias da semana onde a data deve cair
	 * 
	 * @return nova data calculada
 	 */
	Date adicionarDiasUteis(Date data, int numDias, List<Integer> diasSemana);

}
