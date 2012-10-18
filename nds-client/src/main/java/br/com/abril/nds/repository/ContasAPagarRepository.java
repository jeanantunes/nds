package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ContasApagarConsultaPorDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;

public interface ContasAPagarRepository {
	
	Integer pesquisarPorDistribuidorCount(FiltroContasAPagarDTO filtro);
	
	List<ContasApagarConsultaPorDistribuidorDTO> pesquisarPorDistribuidor(FiltroContasAPagarDTO filtro);
}