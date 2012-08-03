package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.dne.UnidadeFederacao;
import br.com.abril.nds.repository.UnidadeFederacaoRepository;

@Repository
public class UnidadeFederacaoRepositoryImpl extends
AbstractRepositoryModel<UnidadeFederacao, String> implements UnidadeFederacaoRepository {

	public UnidadeFederacaoRepositoryImpl() {
		super(UnidadeFederacao.class);
	}

}
