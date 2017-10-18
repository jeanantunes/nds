package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaCouchDTO;

public interface ExporteCouch {
	
	void exportar(List<CotaCouchDTO> listaReoarte, String nomeEntidadeIntegrada);

}
