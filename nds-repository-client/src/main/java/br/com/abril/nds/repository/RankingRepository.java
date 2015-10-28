package br.com.abril.nds.repository;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;

import br.com.abril.nds.dto.RankingDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDTO;


/**
 * 
 * @author Discover Technology
 */
public interface RankingRepository {
	
	Map<Long, RankingDTO> obterRankingCota(FiltroCurvaABCDTO filtro);

	Map<Long, RankingDTO> obterRankingCotaSomenteFaturamento(FiltroCurvaABCDTO filtro);
	
}
