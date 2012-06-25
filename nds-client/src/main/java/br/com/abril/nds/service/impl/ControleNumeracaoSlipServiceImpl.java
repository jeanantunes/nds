package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.TipoSlip;
import br.com.abril.nds.model.movimentacao.ControleNumeracaoSlip;
import br.com.abril.nds.repository.ControleNumeracaoSlipRepository;
import br.com.abril.nds.service.ControleNumeracaoSlipService;

@Service
public class ControleNumeracaoSlipServiceImpl implements ControleNumeracaoSlipService {

	@Autowired
	private ControleNumeracaoSlipRepository controleNumeracaoSlipRepository;
	
	@Transactional
	public Long obterProximoNumeroSlip(TipoSlip tipoSlip) {
		
		ControleNumeracaoSlip controleNumeracaoSlip = controleNumeracaoSlipRepository.obterControleNumeracaoSlip(tipoSlip);
		
		if(controleNumeracaoSlip == null) {
			throw new IllegalStateException("Controle de Numeração de Slip não encontrado");
		}
		
		Long numeroSlip = controleNumeracaoSlip.getProximoNumeroSlip();
		
		if(numeroSlip == null) {
			throw new IllegalStateException("Controle de Numeração de Slip não encontrado");
		}
		
		controleNumeracaoSlip.setProximoNumeroSlip((numeroSlip + 1L));
		
		controleNumeracaoSlipRepository.alterar(controleNumeracaoSlip);
		
		return numeroSlip;
		
	}
}