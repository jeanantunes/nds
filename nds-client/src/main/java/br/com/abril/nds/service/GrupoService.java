package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.GrupoCotaDTO;


public interface GrupoService {
 
	/**
	 * Obtém todos os Grupos
	 * @return List<GrupoCota> grupos
	 */
	List<GrupoCotaDTO> obterTodosGrupos() ;

	void excluirGrupo(Long idGrupo);
}
