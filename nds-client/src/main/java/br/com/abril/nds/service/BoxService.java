package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.Box;

/**
 * Interface que define as regras de acesso a serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Box }  
 * 
 * @author Discover Technology
 */
public interface BoxService {

	/**
	 * Retorna uma lista de boxes referente um determinado produto.
	 * @param codigoProduto - código do propduto
	 * @return List<Box>
	 */
	List<Box> obterBoxPorProduto(String codigoProduto);
}
