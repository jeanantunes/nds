package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.distribuicao.RankingFaturamento;
import br.com.abril.nds.repository.RankingFaturamentoRepository;
import br.com.abril.nds.service.RankingFaturamentoService;

@Service
public class RankingFaturamentoServiceImpl implements RankingFaturamentoService {

	@Autowired
	RankingFaturamentoRepository rankingFaturamentoRepository;
	
	@Transactional
	@Override
	public void executeJobGerarRankingFaturamento() {
		
		this.rankingFaturamentoRepository.deletarRankingFaturamento();
		
		this.rankingFaturamentoRepository.gerarRankingFaturamento();
		
//		this.rankingFaturamentoRepository.gerarRankingFaturamentoParaCotasSemRanking();
		
		this.rankingFaturamentoRepository.atualizarClassificacaoCota();
	}

	@Transactional
	@Override
	public List<RankingFaturamento> buscarTodos() {
		return rankingFaturamentoRepository.buscarTodos();
	}

	@Transactional
	@Override
	public List<RankingFaturamento> buscarPorCota(Cota cota){
		return rankingFaturamentoRepository.buscarPorCota(cota);
	}

}
