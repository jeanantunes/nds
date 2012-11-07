package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoCota;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoCotaRepository;

@Repository
public class FechamentoDiarioConsolidadoCotaRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioConsolidadoCota, Long> implements FechamentoDiarioConsolidadoCotaRepository {
	
	public FechamentoDiarioConsolidadoCotaRepositoryImpl() {
		super(FechamentoDiarioConsolidadoCota.class);
	}
}
