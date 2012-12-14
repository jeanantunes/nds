package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoProdutoEdicao;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do ProdutoEdicao
 * 
 */
public interface HistoricoDescontoProdutoEdicaoRepository extends Repository<HistoricoDescontoProdutoEdicao, Long> {

	/**
	 * Retorna o historico do desconto do ProdutoEdicao
	 * 
	 * @param desconto
	 * @param produtoEdicao
	 * @return
	 */
	HistoricoDescontoProdutoEdicao buscarHistoricoPorDescontoEProduto(Desconto desconto, ProdutoEdicao produtoEdicao);
	
}
