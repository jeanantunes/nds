package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.client.vo.CotaVO;
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
	 * @param politicaCobrancaId
	 * @return CotaUnificacao
	 */
	CotaUnificacao obterCotaUnificacaoPorCotaCentralizadora(Integer numeroCota, Long politicaCobrancaId);
	
    /**
     * Obtem unificacao de Cotas por Cota Centralizada
     * 
     * @param numeroCota
     * @return CotaUnificacao
     */
	CotaUnificacao obterCotaUnificacaoPorCotaCentralizada(Integer numeroCota);

	List<CotaVO> obterCotasCentralizadas(Integer numeroCotaCentralizadora, Long politicaCobrancaId);

	boolean verificarCotaUnificadora(Integer numeroCota, Long politicaCobrancaId);

	boolean verificarCotaUnificada(Integer numeroCota, Long politicaCobrancaId);

	List<Integer> buscarNumeroCotasUnificadoras(Long politicaCobrancaId);

	void removerCotaUnificacao(Long politicaCobrancaId);

	List<CotaUnificacao> obterCotaUnificacaoPorCotaUnificada(Integer numeroCota);
}