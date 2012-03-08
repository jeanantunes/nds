package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.Produto;

public interface ProdutoRepository extends Repository<Produto, Long> {

	List<Produto> obterProdutoLikeNomeProduto(String nome);
	
	List<Produto> obterProdutoPorNomeProduto(String nome);
	
	Produto obterProdutoPorCodigo(String codigoProduto);
}
