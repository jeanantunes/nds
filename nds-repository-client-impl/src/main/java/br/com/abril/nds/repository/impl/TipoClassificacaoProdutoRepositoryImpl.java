package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;

@Repository
public class TipoClassificacaoProdutoRepositoryImpl extends AbstractRepositoryModel<TipoClassificacaoProduto, Long> implements TipoClassificacaoProdutoRepository {

	public TipoClassificacaoProdutoRepositoryImpl() {
		super(TipoClassificacaoProduto.class);
	}

}
