package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.fiscal.ControleNumeracaoNotaFiscal;
import br.com.abril.nds.repository.ControleNumeracaoNotaFiscalRepository;
import br.com.abril.nds.service.ControleNumeracaoNotaFiscalService;

@Service
public class ControleNumeracaoNotaFiscalServiceImpl implements ControleNumeracaoNotaFiscalService {

	@Autowired
	private ControleNumeracaoNotaFiscalRepository controleNumeracaoNotaFiscalRepository;
	
	/**
	 * Obtem o proximo numero de Nota Fiscal disponivel.
	 * 
	 * @param serieNF
	 * 
	 * @return numeroNF - Long
	 */
	@Transactional
	public Long obterProximoNumeroNotaFiscal(String serieNF) {
		
		ControleNumeracaoNotaFiscal controleNumeracaoNotaFiscal = controleNumeracaoNotaFiscalRepository.obterControleNumeracaoNotaFiscal(serieNF);
		
		if(controleNumeracaoNotaFiscal == null) {
			throw new IllegalStateException("Controle de Numeração de Nota Fiscal não encontrado");
		}
		
		Long numeroNF = controleNumeracaoNotaFiscal.getProximoNumeroNF();
		
		if(numeroNF == null) {
			throw new IllegalStateException("Controle de Numeração de Nota Fiscal não encontrado");
		}
		
		controleNumeracaoNotaFiscal.setProximoNumeroNF((numeroNF + 1L));
		
		controleNumeracaoNotaFiscalRepository.alterar(controleNumeracaoNotaFiscal);
		
		return numeroNF;
		
	}
}
