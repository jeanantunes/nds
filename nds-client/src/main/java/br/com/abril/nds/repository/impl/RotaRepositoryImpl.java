package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.repository.RotaRepository;

@Repository
public class RotaRepositoryImpl extends AbstractRepository<Rota, Long>
		implements RotaRepository {

	public RotaRepositoryImpl() {
		super(Rota.class);
	}
}