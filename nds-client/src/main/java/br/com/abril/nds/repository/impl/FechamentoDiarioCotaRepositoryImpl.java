package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioCota;
import br.com.abril.nds.repository.FechamentoDiarioCotaRepository;

@Repository
public class FechamentoDiarioCotaRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioCota, Long> implements FechamentoDiarioCotaRepository {

	public FechamentoDiarioCotaRepositoryImpl() {
		super(FechamentoDiarioCota.class);
	}

}
