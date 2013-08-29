package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.HistoricoEstoqueProduto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoricoEstoqueProdutoRepository;

@Repository
public class HistoricoEstoqueProdutoRepositoryImpl extends AbstractRepositoryModel<HistoricoEstoqueProduto, Long>
		implements HistoricoEstoqueProdutoRepository {

	public HistoricoEstoqueProdutoRepositoryImpl() {
		super(HistoricoEstoqueProduto.class);
	}
}
