package br.com.abril.nds.server.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.repository.impl.AbstractRepositoryModel;
import br.com.abril.nds.server.model.OperacaoDistribuidor;
import br.com.abril.nds.server.repository.OperacaoDistribuidorRepository;

@Repository
public class OperacaoDistribuidorRepositoryImpl extends
		AbstractRepositoryModel<OperacaoDistribuidor, Long> implements OperacaoDistribuidorRepository {

	public OperacaoDistribuidorRepositoryImpl() {
		super(OperacaoDistribuidor.class);
	}
}