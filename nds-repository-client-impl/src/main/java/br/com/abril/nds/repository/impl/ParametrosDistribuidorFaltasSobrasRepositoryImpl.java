package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ParametrosDistribuidorFaltasSobras;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ParametrosDistribuidorFaltasSobrasRepository;

@Repository
public class ParametrosDistribuidorFaltasSobrasRepositoryImpl extends AbstractRepositoryModel<ParametrosDistribuidorFaltasSobras, Long> implements ParametrosDistribuidorFaltasSobrasRepository {

	public ParametrosDistribuidorFaltasSobrasRepositoryImpl() {
		super(ParametrosDistribuidorFaltasSobras.class);
	}

	@Override
	public void alterarOuCriar(ParametrosDistribuidorFaltasSobras parametrosDistribuidorFaltasSobras) {
		getSession().saveOrUpdate(parametrosDistribuidorFaltasSobras);
	}

}
