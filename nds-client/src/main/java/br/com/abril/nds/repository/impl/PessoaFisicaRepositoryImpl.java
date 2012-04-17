package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.repository.PessoaFisicaRepository;

@Repository
public class PessoaFisicaRepositoryImpl extends AbstractRepository<PessoaFisica, Long>
		implements PessoaFisicaRepository {

	public PessoaFisicaRepositoryImpl() {
		super(PessoaFisica.class);
	}
}
