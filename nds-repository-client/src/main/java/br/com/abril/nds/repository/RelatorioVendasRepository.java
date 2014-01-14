package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.dto.RegistroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;

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

	List<RegistroCurvaABCCotaDTO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtro);
	
	Integer obterQtdRegistrosCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtro);
		
	Integer obterQtdRegistrosCurvaABCEditor(FiltroCurvaABCEditorDTO filtro);
	
	Integer obterQtdRegistrosCurvaABCCota(FiltroCurvaABCCotaDTO filtro);
	
}
