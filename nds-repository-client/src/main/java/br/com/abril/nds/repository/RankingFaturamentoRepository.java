package br.com.abril.nds.repository;

import br.com.abril.nds.model.distribuicao.RankingFaturamento;

public interface RankingFaturamentoRepository extends Repository<RankingFaturamento, Long> {

	void executeJobGerarRankingFaturamento();
}
