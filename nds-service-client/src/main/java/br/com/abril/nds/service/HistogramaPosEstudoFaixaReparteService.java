package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.HistogramaPosEstudoAnaliseFaixaReparteDTO;

public interface HistogramaPosEstudoFaixaReparteService {

	HistogramaPosEstudoAnaliseFaixaReparteDTO obterHistogramaPosEstudo( int faixaDe, int faixaAte, Integer estudoId, List<Long> listaIdEdicaoBase);
	
	List<HistogramaPosEstudoAnaliseFaixaReparteDTO> obterListaHistogramaPosEstudo( Integer[][] faixas, Integer estudoId, List<Long> listaIdEdicaoBase);
	
	List<Long> obterIdEdicoesBase (Long idEstudo);
}
