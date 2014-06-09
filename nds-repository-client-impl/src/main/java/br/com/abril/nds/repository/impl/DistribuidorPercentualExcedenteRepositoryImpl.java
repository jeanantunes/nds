package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.DistribuidorPercentualExcedente;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DistribuidorPercentualExcedenteRepository;

@Repository
public class DistribuidorPercentualExcedenteRepositoryImpl extends AbstractRepositoryModel<DistribuidorPercentualExcedente, Long> implements DistribuidorPercentualExcedenteRepository {

	public DistribuidorPercentualExcedenteRepositoryImpl() {
		super(DistribuidorPercentualExcedente.class);
	}

}
