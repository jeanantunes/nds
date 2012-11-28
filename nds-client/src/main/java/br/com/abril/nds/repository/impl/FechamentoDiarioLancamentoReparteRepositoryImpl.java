package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoReparte;
import br.com.abril.nds.repository.FechamentoDiarioLancamentoReparteRepository;

@Repository
public class FechamentoDiarioLancamentoReparteRepositoryImpl extends 
			 AbstractRepositoryModel<FechamentoDiarioLancamentoReparte, Long> implements FechamentoDiarioLancamentoReparteRepository {

	public FechamentoDiarioLancamentoReparteRepositoryImpl() {
		super(FechamentoDiarioLancamentoReparte.class);
	}

}
