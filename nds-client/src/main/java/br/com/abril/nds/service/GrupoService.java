package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaTipoDTO;
import br.com.abril.nds.dto.GrupoCotaDTO;
import br.com.abril.nds.model.cadastro.TipoCota;


public interface GrupoService {
 
	/**
	 * Obtém todos os Grupos
	 * @return List<GrupoCota> grupos
	 */
	List<GrupoCotaDTO> obterTodosGrupos() ;

	void excluirGrupo(Long idGrupo);

	/**
	 * Obtém cotas por Tipo
	 * 
	 * @param tipoCota
	 * @return
	 */
	List<CotaTipoDTO> obterCotaPorTipo(TipoCota tipoCota);

}
