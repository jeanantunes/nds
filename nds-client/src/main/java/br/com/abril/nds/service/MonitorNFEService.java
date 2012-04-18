package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.InfoNfeDTO;
import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;

public interface MonitorNFEService {

	public InfoNfeDTO pesquisarNFe(FiltroMonitorNfeDTO filtro);
	
}
