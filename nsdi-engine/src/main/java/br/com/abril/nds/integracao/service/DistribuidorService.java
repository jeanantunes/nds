package br.com.abril.nds.integracao.service;

import br.com.abril.nds.model.cadastro.Distribuidor;

public interface DistribuidorService {
	
	public Distribuidor findDistribuidor ();
	
	public boolean isDistribuidor(Integer codigo);
	

}
