package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TelefoneDistribuidorRepository;

@Repository
public class TelefoneDistribuidorRepositoryImpl extends AbstractRepositoryModel<TelefoneDistribuidor, Long> implements TelefoneDistribuidorRepository {

	public TelefoneDistribuidorRepositoryImpl() {
		super(TelefoneDistribuidor.class );
	}

}
