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
	
	Map<Long, Long> obterRankingProdutoCota(Long idCota);
	
	Long obterRankingCotaDistribuidor(Long idCota);
	
	Long obterRankingEditor(Long codigoEditor);
}
