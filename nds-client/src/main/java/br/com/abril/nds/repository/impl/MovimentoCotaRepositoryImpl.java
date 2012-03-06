package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.movimentacao.MovimentoEstoqueCota;
import br.com.abril.nds.repository.MovimentoCotaRepository;

@Repository
public class MovimentoCotaRepositoryImpl extends AbstractRepository<MovimentoEstoqueCota, Long> implements MovimentoCotaRepository{

	public MovimentoCotaRepositoryImpl() {
		super(MovimentoEstoqueCota.class);
	}

	
}
