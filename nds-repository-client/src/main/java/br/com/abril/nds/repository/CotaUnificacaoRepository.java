package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.CotaUnificacao;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.CotaUnificacao}  
 * 
 * @author Discover Technology
 *
 */
public interface CotaUnificacaoRepository extends Repository<CotaUnificacao,Long>{

	/**
	 * Obtem Unificacao de Cotas por Cota Centralizadora
	 * 
	 * @param numeroCota
	 * @return CotaUnificacao
	 */
	CotaUnificacao obterCotaUnificacaoPorCotaCentralizadora(Integer numeroCota);
	
	/**
	 * Obtem Unificacao de Cotas por Cota Centralizadora
	 * 
	 * @param idCota
	 * @return CotaUnificacao
	 */
	CotaUnificacao obterCotaUnificacaoPorCotaCentralizadora(Long idCota);
	
	/**
	 * Obtem unificacao de Cotas por Cota Centralizada
	 * 
	 * @param numeroCota
	 * @return CotaUnificacao
	 */
    CotaUnificacao obterCotaUnificacaoPorCotaCentralizada(Integer numeroCota);
	
    /**
     * Obtem unificacao de Cotas por Cota Centralizada
     * 
     * @param idCota
     * @return CotaUnificacao
     */
	CotaUnificacao obterCotaUnificacaoPorCotaCentralizada(Long idCota);
}