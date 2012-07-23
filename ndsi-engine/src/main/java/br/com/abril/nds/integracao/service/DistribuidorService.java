package br.com.abril.nds.integracao.service;

import br.com.abril.nds.dto.DistribuicaoDTO;
import br.com.abril.nds.dto.DistribuidorDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;

public interface DistribuidorService {
	
	public Distribuidor obter ();
	
	public boolean isDistribuidor(Integer codigo);
	
	public void alterar(Distribuidor distribuidor);

	public DistribuidorDTO obterDadosEmissao();

}
