package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.repository.RankingSegmentoRepository;
import br.com.abril.nds.service.RankingSegmentoService;

@Service
public class RankingSegmentoServiceImpl implements RankingSegmentoService {

	@Autowired
	private RankingSegmentoRepository rankingSegmentoRepository;
	
	@Override
	public void executeJobGerarRankingSegmento() {
		rankingSegmentoRepository.executeJobGerarRankingSegmento();

	}


}
