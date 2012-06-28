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
	 * Adiciona dias a uma data e retorna a a data, caso a mesma seja válida,
	 * caso contrário, retorna a próxima data válida.
	 * 
	 * @param data - data a ser adicionada
	 * @param numDias - número de dias
	 * 
	 * @return nova data calculada
 	 */
	Date adicionarDiasRetornarDiaUtil(Date data, int numDias);
	
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
	 * Verifica se a data informada é dia útil.
	 * 
	 * @param data - data para verificação
	 * 
	 * @return indicação se a data é dia útil
 	 */
	boolean isDiaUtil(Date data);
	
	/**
	 * Adiciona dias úteis a uma data.
	 * 
	 * @param data - data a ser adicionada
	 * @param numDias - número de dias
	 * @param diasSemanaConcentracaoCobranca - dias da semana onde a data deve cair
	 * @param diaMesConcentracaoCobranca - dia do mes onde a data deve cair
	 * 
	 * @return nova data calculada
 	 */
	Date adicionarDiasUteis(Date data, int numDias, List<Integer> diasSemanaConcentracaoCobranca, Integer diaMesConcentracaoCobranca);

}
