package br.com.abril.nds.repository;

import br.com.abril.nds.model.financeiro.AcumuloDivida;

/**
 * Repositório de dados referentes ao acumulo de dívidas.
 * 
 * @author Discover Technology
 *
 */
public interface AcumuloDividasRepository extends Repository<AcumuloDivida, Long> {

	/**
	 * Obtém uma acumulo através do ID de um {@link br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota} 
	 * 
	 * @param idMovimentoFinanceiro
	 * 
	 * @return {@link AcumuloDivida}
	 */
	AcumuloDivida obterAcumuloDividaPorMovimentoFinanceiro(Long idMovimentoFinanceiro);
	
	/**
	 * Obtém uma acumulo através do ID de um {@link br.com.abril.nds.model.financeiro.Divida} 
	 * 
	 * @param idDivida
	 * 
	 * @return {@link AcumuloDivida}
	 */
	AcumuloDivida obterAcumuloDividaPorDivida(Long idDivida);
}
