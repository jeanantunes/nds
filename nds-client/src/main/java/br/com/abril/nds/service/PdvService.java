package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;

/**
 * 
 *  Interface que define as regras de implementação referentes a PDVs
 * 
 * @author Discover Technology
 *
 */
public interface PdvService {
	
	/**
	 * Verifica se um pdv pode ser excluído
	 * 
	 * @param idPdv  - identificador do PDV
	 * 
	 * @return boolean
	 */
	boolean isExcluirPdv(Long idPdv);
	
	/**
	 * Retorna uma lista de PDVs referente a uma cota informada.
	 * 
	 * @param filtro - filtro com opçãoe de consulta, ordenação e paginação
	 * 
	 * @return List<PdvDTO>
	 */
	List<PdvDTO> obterPDVsPorCota(FiltroPdvDTO filtro);
}
