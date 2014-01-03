package br.com.abril.nds.repository;

import java.util.Map;

import br.com.abril.nds.dto.RankingDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDTO;


/**
 * 
 * @author Discover Technology
 */
public interface RankingRepository {
	
	Map<Long, RankingDTO> obterRankingProdutoPorCota(FiltroCurvaABCDTO filtro, Long idCota);
	
	Map<Long, RankingDTO> obterRankingCota(FiltroCurvaABCDTO filtro);
	
	Map<Long, RankingDTO> obterRankingEditor(FiltroCurvaABCDTO filtro);
	
	Map<Long, RankingDTO> obterRankingProdutoPorProduto(FiltroCurvaABCDTO filtro);
	
	Map<Long, RankingDTO> obterRankingCotaPorProduto(FiltroCurvaABCDTO filtro, Long idProduto);
	
}
