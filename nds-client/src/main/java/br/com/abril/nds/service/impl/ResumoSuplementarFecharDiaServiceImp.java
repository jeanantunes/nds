package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.repository.ResumoSuplementarFecharDiaRepository;
import br.com.abril.nds.service.ResumoSuplementarFecharDiaService;

@Service
public class ResumoSuplementarFecharDiaServiceImp implements
		ResumoSuplementarFecharDiaService {
	
	@Autowired
	private ResumoSuplementarFecharDiaRepository resumoSuplementarFecharDiaRepository;

	@Override
	@Transactional
	public BigDecimal obterValorEstoqueLogico(Date dataOperacao) {
		return this.resumoSuplementarFecharDiaRepository.obterValorEstoqueLogico(dataOperacao);
	}

}
