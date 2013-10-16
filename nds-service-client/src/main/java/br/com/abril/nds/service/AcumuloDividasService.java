package br.com.abril.nds.service;

import br.com.abril.nds.model.financeiro.AcumuloDivida;

/**
 * Interface para reger a lógica de negócio de Acumulo de Dívidas.  
 * 
 * @author Discover Technology
 *
 */
public interface AcumuloDividasService {
	
	/**
	 * Cria um novo acúmulo de dívidas.
	 * 
	 * @param acumuloDivida
	 */
	void criarAcumuloDivida(AcumuloDivida acumuloDivida);
	
	/**
	 * Atualiza um acúmulo de dívidas.
	 * 
	 * @param acumuloDivida
	 * 
	 * @return {@link AcumuloDivida}
	 */
	AcumuloDivida atualizarAcumuloDivida(AcumuloDivida acumuloDivida);
	
	/**
	 * Obtém uma acumulo através do ID de um {@link br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota} 
	 * 
	 * @param idMovimentoFinanceiro
	 * 
	 * @return {@link AcumuloDivida}
	 */
	AcumuloDivida obterAcumuloDividaPorMovimentoPendente(Long idMovimentoFinanceiro);
	
	/**
	 * Obtém uma acumulo através do ID de um {@link br.com.abril.nds.model.financeiro.Divida} 
	 * 
	 * @param idDivida
	 * 
	 * @return {@link AcumuloDivida}
	 */
	AcumuloDivida obterAcumuloDividaPorDivida(Long idDivida);

}
