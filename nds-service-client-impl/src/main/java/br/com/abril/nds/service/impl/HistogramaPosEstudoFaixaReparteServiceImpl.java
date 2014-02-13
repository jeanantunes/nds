package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.HistogramaPosEstudoAnaliseFaixaReparteDTO;
import br.com.abril.nds.repository.HistogramaPosEstudoRepository;
import br.com.abril.nds.service.HistogramaPosEstudoFaixaReparteService;

@Service
public class HistogramaPosEstudoFaixaReparteServiceImpl implements HistogramaPosEstudoFaixaReparteService {

	@Autowired
	private HistogramaPosEstudoRepository histogramaPosEstudoRepository;
	
	@Transactional(readOnly = true)
	@Override
	public HistogramaPosEstudoAnaliseFaixaReparteDTO obterHistogramaPosEstudo(int faixaDe, int faixaAte, Integer estudoId, List<Long> listaIdEdicaoBase) {
		return histogramaPosEstudoRepository.obterHistogramaPosEstudo(faixaDe, faixaAte, estudoId, listaIdEdicaoBase);
	}
	
	@Transactional
	@Override
	public List<Long> obterIdEdicoesBase(Long idEstudo) {
		return histogramaPosEstudoRepository.obterListaIdProdEdicoesBaseEstudo(idEstudo);
	}

}
