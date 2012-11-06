package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoSuplementar;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoSuplementarRepository;

@Repository
public class FechamentoDiarioConsolidadoSuplementarRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioConsolidadoSuplementar, Long> implements FechamentoDiarioConsolidadoSuplementarRepository {
	
	public FechamentoDiarioConsolidadoSuplementarRepositoryImpl() {
		super(FechamentoDiarioConsolidadoSuplementar.class);
	}
}
