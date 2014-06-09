package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.AnaliseEstudoDTO;
import br.com.abril.nds.dto.filtro.FiltroAnaliseEstudoDTO;

public interface AnaliseEstudoRepository {

	List<AnaliseEstudoDTO> buscarEstudos (FiltroAnaliseEstudoDTO filtro);
	
}
