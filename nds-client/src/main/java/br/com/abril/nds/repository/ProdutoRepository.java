package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.Produto;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Produto}
 * 
 * @author Discover Technology
 */
public interface ProdutoRepository extends Repository<Produto, Long> {

	/**
	 * Obtém produtos cujo nome começa com o nome informado.
	 * 
	 * @param nome - nome do produto
	 * 
	 * @return {@link List<Produto>}
	 */
	List<Produto> obterProdutoLikeNomeProduto(String nome);
	
	/**
	 * Obtém um produto de acordo com o número do produto.
	 * 
	 * @param nome - nome do produto
	 * 
	 * @return {@link Produto}
	 */
	Produto obterProdutoPorNomeProduto(String nome);
	
	/**
	 * Obtém um produto de acordo com o código do produto.
	 * 
	 * @param codigoProduto - código do produto
	 * 
	 * @return {@link Produto}
	 */
	Produto obterProdutoPorCodigo(String codigoProduto);
}
