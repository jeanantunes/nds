package br.com.abril.nds.service;


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
	 * @param produtoEdicao - ProdutoEdicao
	 */
	void gerarPeriodosParcias(Long idProdutoEdicao, Integer qtdePeriodos);
}
