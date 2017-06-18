package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.PickingLEDFullDTO;

public interface LedModelo4IntegracaoService {

	void exportarPickingLED(List<PickingLEDFullDTO> registros, Date dataParametroParaExtracao);

	void processarRetornoPicking();
}
