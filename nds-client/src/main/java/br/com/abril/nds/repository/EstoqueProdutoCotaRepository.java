package br.com.abril.nds.repository;

import java.math.BigDecimal;

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
}
