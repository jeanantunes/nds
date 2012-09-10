package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.FormaCobrancaCaucaoLiquida;
import br.com.abril.nds.repository.FormaCobrancaCaucaoLiquidaRepository;

@Repository
public class FormaCobrancaCaucaoLiquidaRepositoryImpl extends AbstractRepositoryModel<FormaCobrancaCaucaoLiquida,Long> implements FormaCobrancaCaucaoLiquidaRepository  {

	
	/**
	 * Construtor padrão
	 */
	public FormaCobrancaCaucaoLiquidaRepositoryImpl() {
		super(FormaCobrancaCaucaoLiquida.class);
	}
	

}
