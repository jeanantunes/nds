package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.Produto;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Produto}
 * 
 * @author Discover Technology
 */
public interface ProdutoService {

	/**
	 * Obtém um produto de acordo com o nome do produto.
	 * 
	 * @param nomeProduto - nome do produto
	 * 
	 * @return {@link Produto}
	 */
	Produto obterProdutoPorNomeProduto(String nome);
	
	/**
	 * Obtém produtos cujo nome começa com o nome informado.
	 * 
	 * @param nomeProduto - nome do produto
	 * 
	 * @return {@link List<Produto>}
	 */
	List<Produto> obterProdutoLikeNomeProduto(String nome);
	
	/**
	 * Obtém um produto de acordo com o código do produto.
	 * 
	 * @param codigoProduto - código do produto
	 * 
	 * @return {@link Produto}
	 */
	Produto obterProdutoPorCodigo(String codigoProduto);
	
}
