package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;


public interface PdvService {

	boolean isExcluirPdv(Long idPdv);
	
	List<PdvDTO> obterPDVsPorCota(FiltroPdvDTO filtro);
}
