package br.com.abril.nds.service;

import java.util.List;

public interface RankingFaturamentoService {

	void executeJobGerarRankingFaturamento();
	
	List<RankingFaturamento> buscarTodos();
}
