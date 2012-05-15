package br.com.abril.nds.service;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;


/**
 * Interface que define os serviços referentes a Parciais
 * 
 * @author guilherme.morais
 *
 */
public interface ParciaisService {

	/**
	 * Cria novos período parcial para determinada edição de um produto 
	 * 
	 * @param idProdutoEdicao - Id do ProdutoEdicao
	 * @param qtdePeriodos - Quantidade de períodos
	 * @param idUsuario - Id do Usuário
	 */
	void gerarPeriodosParcias(ProdutoEdicao produtoEdicao, Integer qtdePeriodos,Long idUsuario);
}
