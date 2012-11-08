package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoEncalhe;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoEncalheRepository;

@Repository
public class FechamentoDiarioConsolidadoEncalheRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioConsolidadoEncalhe, Long> implements FechamentoDiarioConsolidadoEncalheRepository {
	
	public FechamentoDiarioConsolidadoEncalheRepositoryImpl() {
		super(FechamentoDiarioConsolidadoEncalhe.class);
	}
}
