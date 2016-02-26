package br.com.abril.nds.repository;

import org.hibernate.SQLQuery;

import br.com.abril.nds.model.distribuicao.RankingSegmento;

public interface RankingSegmentoRepository extends Repository<RankingSegmento, Long> {

	void gerarRankingSegmento();
	
	void gerarRankingSegmentoParaCotasSemRanking();
	
	void deletarRankingSegmento();

	void gerarRankingSegmento(Long idSegmento);
}
