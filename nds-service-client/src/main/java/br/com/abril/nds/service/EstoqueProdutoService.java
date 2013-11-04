package br.com.abril.nds.service;

import br.com.abril.nds.model.estoque.EstoqueProduto;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.estoque.EstoqueProduto}  
 * 
 * @author Discover Technology
 *
 */
public interface EstoqueProdutoService {
	
	/**
	 * Obtém o Estoque Produto pelo id do Produto Edição.
	 * 
	 * @param idProdutoEdicao
	 * @return
	 */
	EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao);

}
