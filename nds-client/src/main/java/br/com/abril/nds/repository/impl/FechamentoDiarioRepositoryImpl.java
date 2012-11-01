package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiario;
import br.com.abril.nds.repository.FechamentoDiarioRepository;

@Repository
public class FechamentoDiarioRepositoryImpl extends AbstractRepositoryModel<FechamentoDiario,Long> implements FechamentoDiarioRepository{

	public FechamentoDiarioRepositoryImpl() {
		super(FechamentoDiario.class);
	}
	
}
