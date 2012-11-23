package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoDivida;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoDividaRepository;

@Repository
public class FechamentoDiarioConsolidadoDividaRepositoryImpl extends 
			AbstractRepositoryModel<FechamentoDiarioConsolidadoDivida, Long> implements FechamentoDiarioConsolidadoDividaRepository {

	public FechamentoDiarioConsolidadoDividaRepositoryImpl() {
		super(FechamentoDiarioConsolidadoDivida.class);
	}

}
