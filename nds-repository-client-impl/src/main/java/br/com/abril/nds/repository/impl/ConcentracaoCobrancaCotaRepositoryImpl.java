package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ConcentracaoCobrancaCotaRepository;

@Repository
public class ConcentracaoCobrancaCotaRepositoryImpl extends AbstractRepositoryModel<ConcentracaoCobrancaCota,Long> implements ConcentracaoCobrancaCotaRepository  {

	/**
	 * Construtor padrão
	 */
	public ConcentracaoCobrancaCotaRepositoryImpl() {
		super(ConcentracaoCobrancaCota.class);
	}

}
