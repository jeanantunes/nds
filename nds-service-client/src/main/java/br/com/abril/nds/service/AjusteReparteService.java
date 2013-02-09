package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.AjusteReparteDTO;
import br.com.abril.nds.model.distribuicao.AjusteReparte;


public interface AjusteReparteService {
	void salvarAjuste(AjusteReparte ajuste);

	List<AjusteReparteDTO> buscarCotasEmAjuste(AjusteReparteDTO dto);

}
