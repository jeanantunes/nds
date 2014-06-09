package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.AnaliseEstudoDTO;
import br.com.abril.nds.dto.filtro.FiltroAnaliseEstudoDTO;

public interface AnaliseEstudoService {
	List<AnaliseEstudoDTO> buscarTodosEstudos (FiltroAnaliseEstudoDTO filtro);
	
}
