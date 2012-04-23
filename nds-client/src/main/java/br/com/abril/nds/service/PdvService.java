package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.model.cadastro.PDV;

public interface PdvService {

	boolean isExcluirPdv(Long idPdv);
	
	List<PDV> obterPDVsPorCota(FiltroPdvDTO filtro);
}
