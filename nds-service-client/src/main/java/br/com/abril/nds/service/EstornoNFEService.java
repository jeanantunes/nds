package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.NotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;

public interface EstornoNFEService {

	Long quantidade(FiltroMonitorNfeDTO filtro);
	
	List<NotaFiscalDTO> pesquisar(FiltroMonitorNfeDTO filtro);

	void estornoNotaFiscal(Long id);
	
}
