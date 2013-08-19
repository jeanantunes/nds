package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.BaseReferenciaCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.BaseReferenciaCotaRepository;

@Repository
public class BaseReferenciaCotaRepositoryImpl extends AbstractRepositoryModel<BaseReferenciaCota, Long> implements BaseReferenciaCotaRepository {
	
	public BaseReferenciaCotaRepositoryImpl() {
		super(BaseReferenciaCota.class);
	}
}
