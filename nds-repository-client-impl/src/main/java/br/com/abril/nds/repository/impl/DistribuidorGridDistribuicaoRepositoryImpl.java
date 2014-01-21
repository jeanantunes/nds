package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.DistribuidorGridDistribuicao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DistribuidorGridDistribuicaoRepository;

@Repository
public class DistribuidorGridDistribuicaoRepositoryImpl extends AbstractRepositoryModel<DistribuidorGridDistribuicao, Long> implements DistribuidorGridDistribuicaoRepository {

	public DistribuidorGridDistribuicaoRepositoryImpl() {
		super(DistribuidorGridDistribuicao.class);
	}

}
