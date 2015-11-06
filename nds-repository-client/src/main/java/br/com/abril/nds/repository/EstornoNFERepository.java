package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.NotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;

public interface EstornoNFERepository extends Repository<NotaFiscal, Long>  {
	
	Long quantidade(FiltroMonitorNfeDTO filtro);
	
	List<NotaFiscalDTO> obterListaNotasFiscaisEstorno(FiltroMonitorNfeDTO filtro);
	
}