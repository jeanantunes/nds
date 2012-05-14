package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.repository.TipoDescontoCotaRepository;

@Repository
public class TipoDescontoCotaRepositoryImpl extends AbstractRepository<TipoDescontoCota,Long> implements TipoDescontoCotaRepository {

	public TipoDescontoCotaRepositoryImpl() {
		super(TipoDescontoCota.class);		 
	}

	

}
