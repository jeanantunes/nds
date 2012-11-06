package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioMovimentoVendaSuplementar;
import br.com.abril.nds.repository.FechamentoDiarioMovimentoVendaSuplementarRepository;

@Repository
public class FechamentoDiarioMovimentoVendaSuplementarRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioMovimentoVendaSuplementar, Long> implements FechamentoDiarioMovimentoVendaSuplementarRepository{
	
	public FechamentoDiarioMovimentoVendaSuplementarRepositoryImpl() {
		super(FechamentoDiarioMovimentoVendaSuplementar.class);
	}
}
