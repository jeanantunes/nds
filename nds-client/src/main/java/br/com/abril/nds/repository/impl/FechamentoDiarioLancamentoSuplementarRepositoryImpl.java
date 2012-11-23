package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoSuplementar;
import br.com.abril.nds.repository.FechamentoDiarioLancamentoSuplementarRepository;

@Repository
public class FechamentoDiarioLancamentoSuplementarRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioLancamentoSuplementar, Long> implements FechamentoDiarioLancamentoSuplementarRepository {
	
	public FechamentoDiarioLancamentoSuplementarRepositoryImpl() {
		
		super(FechamentoDiarioLancamentoSuplementar.class);
	}
}
