package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.repository.EnderecoDistribuidorRepository;

@Repository
public class EnderecoDistribuidorRepositoryImpl extends AbstractRepositoryModel<EnderecoDistribuidor, Long> 
											  implements EnderecoDistribuidorRepository {

	/**
	 * Construtor.
	 */
	public EnderecoDistribuidorRepositoryImpl() {
		super(EnderecoDistribuidor.class);
	}
	
}
