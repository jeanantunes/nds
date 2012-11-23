package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoEncalhe;
import br.com.abril.nds.repository.FechamentoDiarioLancamentoEncalheRepository;

@Repository
public class FechamentoDiarioLancamentoEncalheRepositoryImpl extends 
			 AbstractRepositoryModel<FechamentoDiarioLancamentoEncalhe, Long> implements FechamentoDiarioLancamentoEncalheRepository {

	public FechamentoDiarioLancamentoEncalheRepositoryImpl() {
		super(FechamentoDiarioLancamentoEncalhe.class);
	}
}
