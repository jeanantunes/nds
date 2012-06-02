package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.RomaneioDTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;
import br.com.abril.nds.model.cadastro.Box;

public interface RomaneioRepository extends Repository<Box, Long> {
	
	List<RomaneioDTO> buscarRomaneios(FiltroRomaneioDTO filtro);

}
