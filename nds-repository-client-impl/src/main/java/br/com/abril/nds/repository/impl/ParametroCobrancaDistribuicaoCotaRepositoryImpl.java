package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ParametroCobrancaDistribuicaoCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ParametroCobrancaDistribuicaoCotaRepository;

@Repository
public class ParametroCobrancaDistribuicaoCotaRepositoryImpl 
		extends AbstractRepositoryModel<ParametroCobrancaDistribuicaoCota,Long> 
		implements ParametroCobrancaDistribuicaoCotaRepository {

	public ParametroCobrancaDistribuicaoCotaRepositoryImpl() {
		super(ParametroCobrancaDistribuicaoCota.class);
	}
	
}