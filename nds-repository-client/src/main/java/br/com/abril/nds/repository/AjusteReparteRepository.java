package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.AjusteReparteDTO;
import br.com.abril.nds.model.distribuicao.AjusteReparte;

public interface AjusteReparteRepository extends Repository<AjusteReparte, Long> {

	List<AjusteReparteDTO> buscarTodasCotas (AjusteReparteDTO dto);
	
	AjusteReparteDTO buscarPorIdAjuste (Long id);
	
//	void removerPorIdCota (Long numCota);
//	
//	void removerPorCota (Cota cota);
}
