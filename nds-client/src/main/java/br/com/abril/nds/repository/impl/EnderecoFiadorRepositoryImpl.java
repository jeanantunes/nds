package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.EnderecoFiador;
import br.com.abril.nds.repository.EnderecoFiadorRepository;

@Repository
public class EnderecoFiadorRepositoryImpl extends
		AbstractRepository<EnderecoFiador, Long> implements
		EnderecoFiadorRepository {

	public EnderecoFiadorRepositoryImpl() {
		super(EnderecoFiador.class);
	}
}