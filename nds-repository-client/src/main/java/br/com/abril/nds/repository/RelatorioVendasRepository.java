package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.dto.RegistroCurvaABCCotaDTO;
import br.com.abril.nds.dto.RegistroRankingSegmentoDTO;
import br.com.abril.nds.dto.TotalizadorRankingSegmentoDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroRankingSegmentoDTO;

public interface RelatorioVendasRepository {

	public enum TipoPesquisaRanking {
		//Ranking das cotas independente de produto edição
		RankingCota,
		
		//Ranking das cota independente de produto edição 
		//relacionado a um produto especifico
		RankingProduto
	}
	
	public enum AgrupamentoCurvaABC {
		COTA,
		EDITOR,
		PRODUTO_EDICAO
	}
	
	
	List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO, TipoPesquisaRanking tipoPesquisa);

	List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtro);

	List<RegistroHistoricoEditorVO> obterHistoricoEditor(FiltroCurvaABCDTO filtro);
	
	List<RegistroCurvaABCCotaDTO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtro);
	
	List<RegistroRankingSegmentoDTO> obterRankingSegmento(FiltroRankingSegmentoDTO filtro);
	
	TotalizadorRankingSegmentoDTO obterQuantidadeRegistrosRankingSegmento(FiltroRankingSegmentoDTO filtro);

}
