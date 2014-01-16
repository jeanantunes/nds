package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.dto.CotaUnificacaoDTO;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;


/**
 * Interface que define as regras de acesso a serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.CotaUnificacao }  
 * 
 * @author Discover Technology
 */
public interface CotaUnificacaoService {
	
	/**
	 * Salva/Altera Unificação de Cotas
	 * 
	 * @param cotaUnificacaoId
	 * @param numeroCotaCentralizadora
	 * @param numeroCotasCentralizadas
	 */
	void salvarCotaUnificacao(Integer numeroCotaCentralizadora,
			                  List<CotaVO> numeroCotasCentralizadas,
			                  PoliticaCobranca politicaCobranca);
	
	/**
	 * Remove Unificação de Cotas
	 * 
	 * @param politicaCobrancaId
	 */
	void removerCotaUnificacao(Long politicaCobrancaId);
	
	List<CotaVO> obterCotasCentralizadas(Integer numeroCotaCentralizadora, Long idPoliticaCobranca);

	List<CotaUnificacaoDTO> obterCotasUnificadas(Long idPoliticaCobranca);

	CotaVO obterCota(Integer numeroCota, boolean edicao, Long idPoliticaCobranca);
}
