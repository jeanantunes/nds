package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoReparte;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoReparteRepository;

@Repository
public class FechamentoDiarioConsolidadoReparteRepositoryImpl  extends 
		AbstractRepositoryModel<FechamentoDiarioConsolidadoReparte,Long> implements FechamentoDiarioConsolidadoReparteRepository{

	public FechamentoDiarioConsolidadoReparteRepositoryImpl() {
		super(FechamentoDiarioConsolidadoReparte.class);
	}

	
}
