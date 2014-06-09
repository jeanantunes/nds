package br.com.abril.nds.service;

import br.com.abril.nds.model.TipoSlip;

public interface ControleNumeracaoSlipService {

	public Long obterProximoNumeroSlip(TipoSlip tipoSlip);
	
}
