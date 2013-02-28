package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.repository.RankingFaturamentoRepository;
import br.com.abril.nds.service.RankingFaturamentoService;

@Service
public class RankingFaturamentoServiceImpl implements RankingFaturamentoService {

	
	@Autowired
	RankingFaturamentoRepository rankingFaturamentoRepository;
	
	@Override
	public void executeJobGerarRankingFaturamento() {
		this.rankingFaturamentoRepository.executeJobGerarRankingFaturamento();
	}

}
