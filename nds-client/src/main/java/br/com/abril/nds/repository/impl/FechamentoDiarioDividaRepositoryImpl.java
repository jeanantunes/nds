package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioDivida;
import br.com.abril.nds.repository.FechamentoDiarioDividaRepository;

@Repository
public class FechamentoDiarioDividaRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioDivida, Long> implements FechamentoDiarioDividaRepository {

	public FechamentoDiarioDividaRepositoryImpl() {
		super(FechamentoDiarioDivida.class);
	}

}
