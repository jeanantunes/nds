package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.model.cadastro.MaterialPromocional;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.ClusterPDV;
import br.com.abril.nds.model.cadastro.pdv.EspecialidadePDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;

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
	
	void salvar(PdvDTO pdvDTO);
	
	List<TipoPontoPDV> obterTiposPontoPDV();
	
	List<AreaInfluenciaPDV> obterAreasInfluenciaPDV();
	
	List<ClusterPDV> obterClustersPDV();
	
	List<EspecialidadePDV> obterEspecialidadesPDV();
	
	List<TipoGeradorFluxoPDV> obterTiposGeradorFluxo();
	
	List<MaterialPromocional> obterMateriaisPromocionalPDV();
}
