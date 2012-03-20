package br.com.abril.nds.service;

import java.util.Date;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Feriado}  
 * 
 * @author Discover Technology
 */
public interface FeriadoService {
	
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
	 * Obtém o próximo dia útil de acordo com a data informada.
	 * 
	 * @param data - data base
	 * 
	 * @return nova data obtida
 	 */
	Date obterProximoDiaUtil(Date data);

}
