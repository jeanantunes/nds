package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BaseEstudoAnaliseFaixaReparteDTO;
import br.com.abril.nds.repository.HistoramaPosEstudoRepository;
import br.com.abril.nds.service.HistogramaPosEstudoFaixaReparteService;

@Service
public class HistogramaPosEstudoFaixaReparteServiceImpl implements
		HistogramaPosEstudoFaixaReparteService {

	@Autowired
	private HistoramaPosEstudoRepository historamaPosEstudoRepository;
	
	@Transactional(readOnly = true)
	@Override
	public BaseEstudoAnaliseFaixaReparteDTO obterHistogramaPosEstudo(int faixaDe, int faixaAte, Integer estudoId) {
		return historamaPosEstudoRepository.obterHistogramaPosEstudo(faixaDe, faixaAte, estudoId);
	}

}
