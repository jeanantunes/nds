package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.dto.CotaUnificacaoDTO;


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
	 * @param numeroCotaCentralizadora
	 * @param numeroCotasCentralizadas
	 */
	void salvarCotaUnificacao(Integer numeroCotaCentralizadora,
			                  List<Integer> numeroCotasCentralizadas);
	
	
	List<CotaVO> obterCotasCentralizadas(Integer numeroCotaCentralizadora);

	List<CotaUnificacaoDTO> obterCotasUnificadas();

	CotaVO obterCota(Integer numeroCota, boolean edicao);


	void removerCotaUnificacao(Integer numeroCota);
}
