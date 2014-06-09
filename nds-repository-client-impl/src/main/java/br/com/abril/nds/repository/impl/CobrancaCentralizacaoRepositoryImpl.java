package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.CobrancaCentralizacao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.CobrancaCentralizacaoRepository;

@Repository
public class CobrancaCentralizacaoRepositoryImpl extends AbstractRepositoryModel<CobrancaCentralizacao,Long> implements CobrancaCentralizacaoRepository  {

	/**
	 * Construtor padrão
	 */
	public CobrancaCentralizacaoRepositoryImpl() {
		
		super(CobrancaCentralizacao.class);
	}
}