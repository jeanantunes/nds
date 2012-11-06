package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoConsolidadoDivida;
import br.com.abril.nds.repository.FechamentoDiarioResumoConsolidadoDividaRepository;

@Repository
public class FechamentoDiarioResumoConsolidadoDividaRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioResumoConsolidadoDivida, Long> implements FechamentoDiarioResumoConsolidadoDividaRepository {

	public FechamentoDiarioResumoConsolidadoDividaRepositoryImpl() {
		super(FechamentoDiarioResumoConsolidadoDivida.class);
	}

}
