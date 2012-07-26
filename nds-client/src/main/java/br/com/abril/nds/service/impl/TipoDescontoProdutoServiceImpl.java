package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoDescontoProduto;
import br.com.abril.nds.repository.TipoDescontoProdutoRepository;
import br.com.abril.nds.service.TipoDescontoProdutoService;

@Service
public class TipoDescontoProdutoServiceImpl implements TipoDescontoProdutoService {

	@Autowired
	private TipoDescontoProdutoRepository tipoDescontoProdutoRepository;
	
	@Override
	public void incluirDesconto(TipoDescontoProduto tipoDescontoProduto) {
		 this.tipoDescontoProdutoRepository.adicionar(tipoDescontoProduto);

	}

	@Override
	public void excluirDesconto(TipoDescontoProduto tipoDescontoProduto) {
		this.tipoDescontoProdutoRepository.remover(tipoDescontoProduto);

	}

	@Override
	public List<TipoDescontoProdutoDTO> obterTipoDescontoProduto(
			ProdutoEdicao produtoEdicao) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
