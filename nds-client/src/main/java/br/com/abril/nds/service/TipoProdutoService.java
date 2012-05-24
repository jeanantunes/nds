package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.TipoProduto;

/**
 * Interface que possui todos os métodos para o serviço de TipoProduto.
 * 
 * @author Discover Technology
 */
public interface TipoProdutoService {

	/**
	 * Método que retorna todos tipos de Produto.
	 * 
	 * @return List<TipoProduto>
	 */
	List<TipoProduto> obterTodosTiposProduto();
	
}
