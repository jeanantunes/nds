package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ReferenciaCota;
import br.com.abril.nds.repository.ReferenciaCotaRepository;

@Repository
public class ReferenciaCotaRepositoryImpl extends AbstractRepository<ReferenciaCota, Long> implements ReferenciaCotaRepository {

	public ReferenciaCotaRepositoryImpl() {
		super(ReferenciaCota.class);
	}
}
