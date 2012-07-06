package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.repository.LocalidadeRepository;

@Repository
public class LocalidadeRepositoryImpl extends
AbstractRepositoryModel<Localidade, Long> implements LocalidadeRepository {

	public LocalidadeRepositoryImpl() {
		super(Localidade.class);
	}

}
