package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.model.fiscal.ViewNotaFiscal;

public interface ViewNotaFiscalRepository extends Repository<ViewNotaFiscal,Integer>{

	public List<NfeDTO> pesquisarNotaFiscal(FiltroMonitorNfeDTO filtro, boolean indEmitida);

	
}
