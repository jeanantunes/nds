package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.TributoAliquota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TributoAliquotaRepository;

@Repository
public class TributoAliquotaRepositoryImpl extends AbstractRepositoryModel<TributoAliquota, Long> implements TributoAliquotaRepository {

	public TributoAliquotaRepositoryImpl() {
		super(TributoAliquota.class);
	}

}