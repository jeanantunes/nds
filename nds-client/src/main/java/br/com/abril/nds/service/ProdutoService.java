package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.Produto;

public interface ProdutoService {

	List<Produto> obterProdutoPorNomeProduto(String nome);
	
	Produto obterProdutoPorCodigo(String codigoProduto);
	
}
