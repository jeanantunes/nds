package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.movimentacao.FuroProduto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FuroProdutoRepository;

@Repository
public class FuroProdutoRepositoryImpl extends AbstractRepositoryModel<FuroProduto, Long>
		implements FuroProdutoRepository {

	public FuroProdutoRepositoryImpl() {
		super(FuroProduto.class);
	}
}
