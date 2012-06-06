package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.RomaneioDTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;

public interface RomaneioService {
	
	List<RomaneioDTO> buscarRomaneio(FiltroRomaneioDTO filtro, String limitar);

	Integer buscarTotalDeRomaneios(FiltroRomaneioDTO filtro);

}
