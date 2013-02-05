package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ParametrosDistribuidorEmissaoDocumentoRepository;

@Repository
public class ParametrosDistribuidorEmissaoDocumentoRepositoryImpl extends AbstractRepositoryModel<ParametrosDistribuidorEmissaoDocumento, Long> implements ParametrosDistribuidorEmissaoDocumentoRepository {

	public ParametrosDistribuidorEmissaoDocumentoRepositoryImpl() {
		super(ParametrosDistribuidorEmissaoDocumento.class);
	}

	@Override
	public void alterarOuCriar(ParametrosDistribuidorEmissaoDocumento parametrosDistribuidorEmissaoDocumento) {
		getSession().saveOrUpdate(parametrosDistribuidorEmissaoDocumento);
	}

}
