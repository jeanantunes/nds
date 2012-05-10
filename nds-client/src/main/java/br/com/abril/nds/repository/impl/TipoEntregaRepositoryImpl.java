package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.repository.TipoEntregaRepository;

@Repository
public class TipoEntregaRepositoryImpl   extends AbstractRepository<TipoEntrega, Long> implements TipoEntregaRepository {

	public TipoEntregaRepositoryImpl() {
		super(TipoEntrega.class);
	}

}
