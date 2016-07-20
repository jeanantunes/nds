package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.PickingLEDFullDTO;

public interface LedModelo4IntegracaoService {

	void exportarPickingLED(List<PickingLEDFullDTO> registros, Date dataParametroParaExtracao);
}
