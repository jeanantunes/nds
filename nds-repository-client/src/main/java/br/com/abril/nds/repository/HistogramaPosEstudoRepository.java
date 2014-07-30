package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.HistogramaPosEstudoAnaliseFaixaReparteDTO;

public interface HistogramaPosEstudoRepository {
	
	HistogramaPosEstudoAnaliseFaixaReparteDTO obterHistogramaPosEstudo(Integer estudoId, List<Long> listaIdEdicaoBase, Integer[] faixa);
	
	List<HistogramaPosEstudoAnaliseFaixaReparteDTO> obterHistogramaPosEstudo(Integer estudoId, List<Long> listaIdEdicaoBase, Integer[]... faixas);
	
	List<Long> obterListaIdProdEdicoesBaseEstudo (Long idEstudo);
}
