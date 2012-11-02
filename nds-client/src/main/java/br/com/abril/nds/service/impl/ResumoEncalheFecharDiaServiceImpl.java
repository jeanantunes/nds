package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.repository.ResumoEncalheFecharDiaRepository;
import br.com.abril.nds.service.ResumoEncalheFecharDiaService;


@Service
public class ResumoEncalheFecharDiaServiceImpl implements ResumoEncalheFecharDiaService {
	
	@Autowired
	private ResumoEncalheFecharDiaRepository resumoEncalheFecharDiaRepository;

	@Override
	@Transactional
	public BigDecimal obterValorEncalheFisico(Date dataOperacao, boolean juramentada) {		 
		return this.resumoEncalheFecharDiaRepository.obterValorEncalheFisico(dataOperacao,juramentada);
	}

	@Override
	@Transactional
	public BigDecimal obterValorEncalheLogico(Date dataOperacao) {		 
		return this.resumoEncalheFecharDiaRepository.obterValorEncalheLogico(dataOperacao);
	}

}
