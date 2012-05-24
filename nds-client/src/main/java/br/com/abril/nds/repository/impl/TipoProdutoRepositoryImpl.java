package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.repository.TipoProdutoRepository;

/**
 * 
 * @author Discover Technology
 *
 */
@Repository
public class TipoProdutoRepositoryImpl extends AbstractRepository<TipoProduto, Long> 
									   implements TipoProdutoRepository {

	public TipoProdutoRepositoryImpl() {
		super(TipoProduto.class);
	}

}
