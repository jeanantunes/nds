package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.distribuicao.RankingFaturamento;

public interface RankingFaturamentoRepository extends Repository<RankingFaturamento, Long> {

	void executeJobGerarRankingFaturamento();
	
	public List<RankingFaturamento>  buscarPorCota(Cota cota);
}
