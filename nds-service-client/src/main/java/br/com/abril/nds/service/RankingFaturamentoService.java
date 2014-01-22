
package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.distribuicao.RankingFaturamento;

public interface RankingFaturamentoService {

	void executeJobGerarRankingFaturamento();
	
	List<RankingFaturamento> buscarTodos();
	
	public List<RankingFaturamento> buscarPorCota(Cota cota);
	
}
