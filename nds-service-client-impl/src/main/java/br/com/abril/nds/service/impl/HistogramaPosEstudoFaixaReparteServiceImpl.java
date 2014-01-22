package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.HistogramaPosEstudoAnaliseFaixaReparteDTO;
import br.com.abril.nds.repository.HistogramaPosEstudoRepository;
import br.com.abril.nds.service.HistogramaPosEstudoFaixaReparteService;

@Service
public class HistogramaPosEstudoFaixaReparteServiceImpl implements
		HistogramaPosEstudoFaixaReparteService {

	@Autowired
	private HistogramaPosEstudoRepository histogramaPosEstudoRepository;
	
	@Transactional(readOnly = true)
	@Override
	public HistogramaPosEstudoAnaliseFaixaReparteDTO obterHistogramaPosEstudo(int faixaDe, int faixaAte, Integer estudoId) {
		return histogramaPosEstudoRepository.obterHistogramaPosEstudo(faixaDe, faixaAte, estudoId);
	}

}
