package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.repository.EnderecoRepository;

@Repository
public class EnderecoRepositoryImpl extends AbstractRepository<Endereco, Long> implements
		EnderecoRepository {

	public EnderecoRepositoryImpl() {
		super(Endereco.class);
	}
}
