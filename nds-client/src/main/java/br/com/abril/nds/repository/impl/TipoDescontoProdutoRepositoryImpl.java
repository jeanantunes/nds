package br.com.abril.nds.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.model.cadastro.EspecificacaoDesconto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.model.cadastro.TipoDescontoProduto;
import br.com.abril.nds.repository.TipoDescontoProdutoRepository;

@Repository
public class TipoDescontoProdutoRepositoryImpl extends AbstractRepositoryModel<TipoDescontoProduto,Long>  implements
		TipoDescontoProdutoRepository {



	public TipoDescontoProdutoRepositoryImpl() {
		super(TipoDescontoProduto.class);
	}

	@Override
	public void incluirDesconto(TipoDescontoProduto tipoDescontoProduto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TipoDescontoProdutoDTO> obterTipoDescontoProduto(
			ProdutoEdicao produtoEdicao) {
		// TODO Auto-generated method stub
		return null;
	}

}
