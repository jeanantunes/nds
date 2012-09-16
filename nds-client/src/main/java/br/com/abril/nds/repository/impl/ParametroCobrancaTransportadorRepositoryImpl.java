package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ParametroCobrancaTransportador;
import br.com.abril.nds.repository.ParametroCobrancaTransportadorRepository;

@Repository
public class ParametroCobrancaTransportadorRepositoryImpl 
		extends AbstractRepositoryModel<ParametroCobrancaTransportador,Long> 
		implements ParametroCobrancaTransportadorRepository {

	public ParametroCobrancaTransportadorRepositoryImpl() {
		super(ParametroCobrancaTransportador.class);
	}
}