package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.model.cadastro.Cota;
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
     * Obtem unificacao de Cotas por Cota Centralizada
     * 
     * @param numeroCota
     * @return CotaUnificacao
     */
	CotaUnificacao obterCotaUnificacaoPorCotaCentralizada(Integer numeroCota);

	List<CotaVO> obterCotasCentralizadas(Integer numeroCotaCentralizadora);

	boolean verificarCotaUnificadora(Integer numeroCota);

	boolean verificarCotaUnificada(Integer numeroCota);

	List<Integer> buscarNumeroCotasUnificadoras();

	List<CotaUnificacao> obterCotaUnificacaoPorCotaUnificada(Integer numeroCota);

	Cota obterCotaUnificadoraPorCota(Integer numeroCota);

	CotaUnificacao obterCotaUnificacaoPorCotaCentralizadora(Long idCota);

    Long obterIdCotaUnificadoraPorCota(Long idCota);
}