package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaConsignadaCouchDTO;
import br.com.abril.nds.dto.CotaCouchDTO;

public interface ExporteCouch {
	
	void exportarLancamentoRecolhimento(List<CotaCouchDTO> listaReoarte, String nomeEntidadeIntegrada);
	void exportarcotaConsignada(List<CotaConsignadaCouchDTO> listaCotaConsignada);
	
}
