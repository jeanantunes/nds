package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.repository.RankingSegmentoRepository;
import br.com.abril.nds.repository.TipoSegmentoProdutoRepository;
import br.com.abril.nds.service.RankingSegmentoService;

@Service
public class RankingSegmentoServiceImpl implements RankingSegmentoService {

	@Autowired
	private RankingSegmentoRepository rankingSegmentoRepository;
	
	@Autowired
	private TipoSegmentoProdutoRepository tipoSegmentoProdutoRepository; 
	
	@Override
	@Transactional
	public void executeJobGerarRankingSegmento() {
		
		this.rankingSegmentoRepository.deletarRankingSegmento();
		
		List<TipoSegmentoProduto> segmentos = tipoSegmentoProdutoRepository.obterTipoSegmentoProdutoOrdenados(null);
		
		for (TipoSegmentoProduto tipoSegmentoProduto : segmentos) {
			if(tipoSegmentoProduto.getId() != null){
				this.rankingSegmentoRepository.gerarRankingSegmento(tipoSegmentoProduto.getId());
			}
		}
		
//		this.rankingSegmentoRepository.gerarRankingSegmento();

//		this.rankingSegmentoRepository.gerarRankingSegmentoParaCotasSemRanking();
	}

}