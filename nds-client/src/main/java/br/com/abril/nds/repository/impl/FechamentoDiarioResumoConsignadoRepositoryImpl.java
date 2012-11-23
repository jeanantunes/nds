package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoConsignado;
import br.com.abril.nds.repository.FechamentoDiarioResumoConsignadoRepository;

@Repository
public class FechamentoDiarioResumoConsignadoRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioResumoConsignado, Long> implements FechamentoDiarioResumoConsignadoRepository {

	public FechamentoDiarioResumoConsignadoRepositoryImpl() {
	
		super(FechamentoDiarioResumoConsignado.class);
	}
}
