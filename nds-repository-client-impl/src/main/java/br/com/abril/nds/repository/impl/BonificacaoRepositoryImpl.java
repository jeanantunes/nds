package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.Bonificacao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.BonificacaoRepository;

@Repository
public class BonificacaoRepositoryImpl extends AbstractRepositoryModel<Bonificacao, Long> implements BonificacaoRepository {

	public BonificacaoRepositoryImpl() {
		super(Bonificacao.class);
	}

}
