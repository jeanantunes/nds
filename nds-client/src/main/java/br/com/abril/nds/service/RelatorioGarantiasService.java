package br.com.abril.nds.service;

import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.RelatorioDetalheGarantiaDTO;
import br.com.abril.nds.dto.RelatorioGarantiasDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioGarantiasDTO;

public interface RelatorioGarantiasService {

	
	FlexiGridDTO<RelatorioGarantiasDTO> gerarTodasGarantias(FiltroRelatorioGarantiasDTO filtro);
	
	FlexiGridDTO<RelatorioDetalheGarantiaDTO> gerarPorTipoGarantia(FiltroRelatorioGarantiasDTO filtro);
}
