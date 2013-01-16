package br.com.abril.nds.service;

import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;

public interface CotaBaseService {
	
	FiltroCotaBaseDTO obterDadosFiltro(Integer numeroCota);

}
