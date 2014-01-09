package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.CotaUnificacao;


/**
 * Interface que define as regras de acesso a serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.CotaUnificacao }  
 * 
 * @author Discover Technology
 */
public interface CotaUnificacaoService {
	
	/**
	 * Valida na alteração se Cota já está em outra Centralização como Cota Centralizada ou Centralizadora
	 * 
	 * @param cotaUnificacaoId
	 * @param numeroCota
	 */
	void validaAlteracaoUnificacaoCota(Long cotaUnificacaoId, Integer numeroCota);
	
	/**
	 * Valida na inclusão se Cota já está em outra Centralização como Cota Centralizada ou Centralizadora
	 * 
	 * @param numeroCota
	 */
	void validaNovaUnificacaoCota(Integer numeroCota);

	/**
	 * Salva/Altera Unificação de Cota
	 * 
	 * @param cotaUnificacaoId
	 * @param numeroCotaCentralizadora
	 * @param numeroCotaCentralizada
	 */
	public void salvarCotaUnificacao(Long cotaUnificacaoId,
			                         Integer numeroCotaCentralizadora, 
			                         Integer numeroCotaCentralizada);
	
	/**
	 * Salva/Altera Unificação de Cotas
	 * 
	 * @param cotaUnificacaoId
	 * @param numeroCotaCentralizadora
	 * @param numeroCotasCentralizadas
	 */
	void salvarCotaUnificacao(Long cotaUnificacaoId,
							  Integer numeroCotaCentralizadora,
			                  List<Integer> numeroCotasCentralizadas);
	
	/**
	 * Remove Unificação de Cotas
	 * 
	 * @param cotaUnificacaoId
	 */
	public void removerCotaUnificacao(Long cotaUnificacaoId);
	
	/**
	 * Verifica se Cota é Centralizadora
	 * 
	 * @param numeroCota
	 * @return boolean
	 */
	public boolean isCotaCentralizadora(Integer numeroCota);
	
	/**
	 * Verifica se Cota é Centralizada
	 * 
	 * @param numeroCota
	 * @return boolean
	 */
	public boolean isCotaCentralizada(Integer numeroCota);
	
	/**
	 * Verifica se Cota é Centralizadora
	 * 
	 * @param idCota
	 * @return boolean
	 */
    public boolean isCotaCentralizadora(Long idCota);
	
    /**
	 * Verifica se Cota é Centralizada
	 * 
	 * @param idCota
	 * @return boolean
	 */
	public boolean isCotaCentralizada(Long idCota);
	
	/**
	 * Obtem Cota Unificacao por Cota Centralizadora
	 * 
	 * @param numeroCota
	 * @return CotaUnificacao
	 */
	public CotaUnificacao obterCotaUnificacaoPorCotaCentralizadora(Integer numeroCota);
	
	/**
	 * Obtem Cota Unificacao por Cota Centralizada
	 * 
	 * @param numeroCota
	 * @return CotaUnificacao
	 */
	public CotaUnificacao obterCotaUnificacaoPorCotaCentralizada(Integer numeroCota);
	
	/**
	 * Obtem Cota Unificacao por Cota Centralizadora
	 * 
	 * @param idCota
	 * @return CotaUnificacao
	 */
    public CotaUnificacao obterCotaUnificacaoPorCotaCentralizadora(Long idCota);
	
    /**
	 * Obtem Cota Unificacao por Cota Centralizada
	 * 
	 * @param idCota
	 * @return CotaUnificacao
	 */
	public CotaUnificacao obterCotaUnificacaoPorCotaCentralizada(Long idCota);
}