package br.com.abril.nds.repository;

import br.com.abril.nds.dto.HistogramaPosEstudoAnaliseFaixaReparteDTO;

public interface HistoramaPosEstudoRepository {

	HistogramaPosEstudoAnaliseFaixaReparteDTO obterHistogramaPosEstudo( int faixaDe, int faixaAte, Integer estudoId);
	
}
