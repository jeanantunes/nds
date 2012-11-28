package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoAvista;
import br.com.abril.nds.repository.FechamentoDiarioResumoAvistaRepository;

@Repository
public class FechamentoDiarioResumoAvistaRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioResumoAvista, Long> implements FechamentoDiarioResumoAvistaRepository {
	
	public FechamentoDiarioResumoAvistaRepositoryImpl() {
		
		super(FechamentoDiarioResumoAvista.class);
	}
}
