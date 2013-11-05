package br.com.abril.nds.repository;

import java.util.Map;


/**
 * 
 * @author Discover Technology
 */
public interface RankingRepository {
	
	Map<Long, Long> obterRankingProdutoPorCota(Long idCota);
	
	Map<Long, Long> obterRankingCota();
	
	Map<Long, Long> obterRankingEditor();
	
	Map<Long, Long> obterRankingProdutoPorProduto();
	
	Map<Long, Long> obterRankingCotaPorProduto(Long idProduto);
	
}
