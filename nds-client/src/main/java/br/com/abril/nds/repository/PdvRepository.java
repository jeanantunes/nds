package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.model.cadastro.PDV;


public interface PdvRepository extends Repository<PDV, Long> {

	List<PDV> obterPDVsPorCota(FiltroPdvDTO filtro);
}
