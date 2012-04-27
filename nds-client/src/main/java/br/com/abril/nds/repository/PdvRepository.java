package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.model.cadastro.pdv.PDV;

/**
 * 
 * Interface que define as regras de implementação referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.pdv.PDV}  
 * 
 * 
 * @author Discover Technology
 *
 */
public interface PdvRepository extends Repository<PDV, Long> {
	
	/**
	 * Retorna uma lista de PDVs de uma determinda cota.
	 * 
	 * @param filtro - filtro com os parâmetros de consulta de PDVs
	 * 
	 * @return List<PdvDTO>
	 */
	List<PdvDTO> obterPDVsPorCota(FiltroPdvDTO filtro);
}
