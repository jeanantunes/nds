package br.com.abril.nds.repository;

import br.com.abril.nds.dto.BaseEstudoAnaliseFaixaReparteDTO;

public interface HistoramaPosEstudoRepository {

	BaseEstudoAnaliseFaixaReparteDTO obterHistogramaPosEstudo( int faixaDe, int faixaAte, Integer estudoId);
	
}
