package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;

/**
 * @author Discover Technology
 */
public interface AlteracaoCotaService {

	List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO); 
	
	/**
	 * Retorna a quantidade de Alterações Cotas pelos critérios de pesquisa
	 * especificados.
	 * 
	 * @param filtroAlteracaoCotaDTO
	 * 
	 * @return
	 */
	int contarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO);
	
	FiltroAlteracaoCotaDTO preencherFiltroAlteracaoCotaDTO(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO);
}
