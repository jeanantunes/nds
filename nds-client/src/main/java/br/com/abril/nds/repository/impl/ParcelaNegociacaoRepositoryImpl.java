package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.ParcelaNegociacao;
import br.com.abril.nds.repository.ParcelaNegociacaoRepository;

@Repository
public class ParcelaNegociacaoRepositoryImpl extends AbstractRepositoryModel<ParcelaNegociacao, Long> 
											 implements ParcelaNegociacaoRepository {

	public ParcelaNegociacaoRepositoryImpl() {
		super(ParcelaNegociacao.class);
	}
}