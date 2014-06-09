package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.HistogramaPosEstudoAnaliseFaixaReparteDTO;

public interface HistogramaPosEstudoRepository {

	HistogramaPosEstudoAnaliseFaixaReparteDTO obterHistogramaPosEstudo( int faixaDe, int faixaAte, Integer estudoId, List<Long> listaIdEdicaoBase);
	
	List<Long> obterListaIdProdEdicoesBaseEstudo (Long idEstudo);
}
