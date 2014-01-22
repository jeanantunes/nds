package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.DistribuidorClassificacaoCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DistribuidorClassificacaoCotaRepository;

@Repository
public class DistribuidorClassificacaoCotaRepositoryImpl extends AbstractRepositoryModel<DistribuidorClassificacaoCota, Long> implements DistribuidorClassificacaoCotaRepository {

	public DistribuidorClassificacaoCotaRepositoryImpl() {
		super(DistribuidorClassificacaoCota.class);
	}

}
