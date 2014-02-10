package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.repository.RankingSegmentoRepository;
import br.com.abril.nds.service.RankingSegmentoService;

@Service
public class RankingSegmentoServiceImpl implements RankingSegmentoService {

	@Autowired
	private RankingSegmentoRepository rankingSegmentoRepository;
	
	@Override
	@Transactional
	public void executeJobGerarRankingSegmento() {
		rankingSegmentoRepository.executeJobGerarRankingSegmento();

	}

}