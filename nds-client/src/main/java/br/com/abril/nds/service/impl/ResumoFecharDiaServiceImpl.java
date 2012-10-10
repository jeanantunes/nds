package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.repository.ResumoFecharDiaRepository;
import br.com.abril.nds.service.ResumoFecharDiaService;

@Service
public class ResumoFecharDiaServiceImpl  implements ResumoFecharDiaService {

	@Autowired
	private ResumoFecharDiaRepository resumoFecharDiaRepository;
	
	@Override
	@Transactional
	public BigDecimal obterValorReparte(Date dataOperacaoDistribuidor) {		
		return resumoFecharDiaRepository.obterValorReparte(dataOperacaoDistribuidor);
	}

}
