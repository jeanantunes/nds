package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioMovimentoVendaEncalhe;
import br.com.abril.nds.repository.FechamentoDiarioMovimentoVendaEncalheRepository;

@Repository
public class FechamentoDiarioMovimentoVendaEncalheRepositoryImpl extends 
			 AbstractRepositoryModel<FechamentoDiarioMovimentoVendaEncalhe, Long> implements FechamentoDiarioMovimentoVendaEncalheRepository{
	
	public FechamentoDiarioMovimentoVendaEncalheRepositoryImpl() {
		super(FechamentoDiarioMovimentoVendaEncalhe.class);
	}
}
