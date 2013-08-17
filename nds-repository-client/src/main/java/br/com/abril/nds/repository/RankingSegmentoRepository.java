package br.com.abril.nds.repository;

import br.com.abril.nds.model.distribuicao.RankingSegmento;

public interface RankingSegmentoRepository extends Repository<RankingSegmento, Long> {

	void executeJobGerarRankingSegmento();
}
