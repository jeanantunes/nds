package br.com.abril.nds.integracao.service;

import java.util.List;

import br.com.abril.nds.dto.DistribuidorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoStatusGarantia;

public interface DistribuidorService {
	
	public Distribuidor obter ();
	
	public boolean isDistribuidor(Integer codigo);
	
	public void alterar(Distribuidor distribuidor);

	public DistribuidorDTO obterDadosEmissao();

	List<String> obterNomeCNPJDistribuidor();
	
	List<ItemDTO<TipoGarantia, String>> getComboTiposGarantia();
	
	List<ItemDTO<TipoStatusGarantia, String>> getComboTiposStatusGarantia();
	
}