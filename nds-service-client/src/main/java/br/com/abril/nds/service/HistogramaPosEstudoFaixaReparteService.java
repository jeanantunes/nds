package br.com.abril.nds.service;

import br.com.abril.nds.dto.HistogramaPosEstudoAnaliseFaixaReparteDTO;

public interface HistogramaPosEstudoFaixaReparteService {

	HistogramaPosEstudoAnaliseFaixaReparteDTO obterHistogramaPosEstudo( int faixaDe, int faixaAte, Integer estudoId);
	
}
