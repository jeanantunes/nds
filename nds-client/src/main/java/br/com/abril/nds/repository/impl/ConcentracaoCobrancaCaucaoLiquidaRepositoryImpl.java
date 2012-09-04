package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCaucaoLiquida;
import br.com.abril.nds.repository.ConcentracaoCobrancaCaucaoLiquidaRepository;

@Repository
public class ConcentracaoCobrancaCaucaoLiquidaRepositoryImpl extends AbstractRepositoryModel<ConcentracaoCobrancaCaucaoLiquida,Long> implements ConcentracaoCobrancaCaucaoLiquidaRepository  {

	/**
	 * Construtor padrão
	 */
	public ConcentracaoCobrancaCaucaoLiquidaRepositoryImpl() {
		super(ConcentracaoCobrancaCaucaoLiquida.class);
	}

}
