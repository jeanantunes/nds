package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.model.estoque.EstoqueProdutoCota;

public interface EstoqueProdutoCotaRepository extends Repository<EstoqueProdutoCota, Long>{

	EstoqueProdutoCota buscarEstoquePorProdutoECota(Long idProdutoEdicao, Long idCota);
	
	EstoqueProdutoCota buscarEstoquePorProdutEdicaoECota(Long idProdutoEdicao, Long idCota);
	
	/**
	 * Retorna a quantidade efetiva de produtos edição em estoque.
	 * 
	 * @param numeroEdicao - número edição
	 * @param codigoProduto - código do propduto
	 * @param numeroCota - número da cota
	 * @return BigDecimal 
	 */
	BigDecimal buscarQuantidadeEstoqueProdutoEdicao(Long numeroEdicao, String codigoProduto ,Integer numeroCota);
	
	/**
	 * Obtém o valor total de reparte para cota especifica
	 * 
	 * @param numeroCota
	 * @param listaIdProdutoEdicao
	 * @param idDistribuidor
	 * @param idCota
	 * @param idProdutoEdicao
	 * 
	 * @return BigDecimal
	 */
	BigDecimal obterValorTotalReparteCota(
			Integer numeroCota, 
			List<Long> listaIdProdutoEdicao, 
			Long idDistribuidor,
			Long idCota,
			Long idProdutoEdicao);
}
