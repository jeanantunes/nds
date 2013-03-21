package br.com.abril.nds.service;

import br.com.abril.nds.dto.BaseEstudoAnaliseFaixaReparteDTO;

public interface HistogramaPosEstudoFaixaReparteService {

	BaseEstudoAnaliseFaixaReparteDTO obterHistogramaPosEstudo( int faixaDe, int faixaAte, Integer estudoId);
	
}
