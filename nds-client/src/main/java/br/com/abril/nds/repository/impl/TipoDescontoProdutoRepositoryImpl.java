package br.com.abril.nds.repository.impl;

import java.util.List;

import br.com.abril.nds.model.cadastro.EspecificacaoDesconto;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.model.cadastro.TipoDescontoProduto;
import br.com.abril.nds.repository.TipoDescontoProdutoRepository;

public class TipoDescontoProdutoRepositoryImpl extends AbstractRepository<TipoDescontoProduto,Long>  implements
		TipoDescontoProdutoRepository {



	public TipoDescontoProdutoRepositoryImpl() {
		super(TipoDescontoProduto.class);
	}

	@Override
	public int obterSequencial() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<TipoDescontoCota> obterTipoDescontosCotas(
			EspecificacaoDesconto especificacaoDesconto) {
		// TODO Auto-generated method stub
		return null;
	}

}
