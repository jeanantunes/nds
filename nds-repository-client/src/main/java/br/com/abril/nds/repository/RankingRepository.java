package br.com.abril.nds.repository;

import java.util.Map;


/**
 * 
 * @author Discover Technology
 */
public interface RankingRepository {
	
	/**
	 * Retorna a posição do ranking do produto em relação a vendas do mesmo
	 * 
	 * @param idProduto - identificador do produto
	 * 
	 * @return Long - posição do ranking do produto
	 */
	Long obterRankingProduto(Long idProduto);
	
	/**
	 * Retorna a posição do ranking da cota em relação a um determinado produto
	 * 
	 * @param idProduto - identificador do produto
	 * 
	 * @param idCota identificador da cota
	 * 
	 * @return Long - posição do ranking da cota
	 */
	Long obterRankingCota(Long idProduto,Long idCota);
	
	Map<Long, Long> obterRankingProdutoPorCota(Long idCota);
	
	Map<Long, Long> obterRankingCota();
	
	Map<Long, Long> obterRankingEditor();
	
	Map<Long, Long> obterRankingProdutoPorProduto(Long idProdutoEdicao);
	
	Map<Long, Long> obterRankingCotaPorProduto(Long idProdutoEdicao);
	
	Long obterRankingCotaDistribuidor(Long idCota);
	
	Long obterRankingEditor(Long codigoEditor);
}
