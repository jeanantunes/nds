package br.com.abril.xrequers.integration.repository.tests;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.TipoProdutoRepository;

public abstract class ProdutoRepositoryTest extends AbstractRepositoryTest {
	
	@Autowired
	protected ProdutoRepository produtoRepository;
	
	@Autowired
	protected TipoProdutoRepository tipoProdutoRepository;
	
}
