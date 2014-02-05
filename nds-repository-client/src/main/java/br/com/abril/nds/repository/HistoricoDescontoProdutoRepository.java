package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoProduto;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do Produto
 * 
 * @author Discover Technology
 */
public interface HistoricoDescontoProdutoRepository extends Repository<HistoricoDescontoProduto, Long> {

	/**
	 * Retorna o historico do desconto do Produto
	 * 
	 * @param idDesconto
	 * @param idProduto
	 * 
	 * @return HistoricoDescontoProduto
	 */
	HistoricoDescontoProduto buscarHistoricoPorDescontoEProduto(Long idDesconto, Long idProduto);
	
}
