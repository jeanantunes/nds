package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaBaseDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.model.cadastro.Cota;

public interface CotaBaseService {
	
	FiltroCotaBaseDTO obterDadosFiltro(Integer numeroCota);
	
	List<CotaBaseDTO> obterCotasBases(Cota cotaNova);

}
