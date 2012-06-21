package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;

public interface ConsultaConsignadoCota {
	
	List<ConsultaConsignadoCotaDTO> buscarConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro, String limitar);

}
