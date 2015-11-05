package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.dto.RegistroCurvaABCCotaDTO;
import br.com.abril.nds.dto.RegistroRankingSegmentoDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroRankingSegmentoDTO;

/**
 * Interface de serviços de curvas de relatórios de vendas
 * @author InfoA2
 */

public interface RelatorioVendasService {

	List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO);

	List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO);
	
	List<RegistroHistoricoEditorVO> obterHistoricoEditor(FiltroPesquisarHistoricoEditorDTO filtroCurvaABCEditorDTO);
	
	List<RegistroCurvaABCDistribuidorVO> obterCurvaABCProduto(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO);
	
	List<RegistroCurvaABCCotaDTO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtroCurvaABCCotaDTO);
	
	List<RegistroRankingSegmentoDTO> obterRankingSegmento(FiltroRankingSegmentoDTO filtro);

	void buscarQuantidadeDePdvs(List<RegistroCurvaABCDistribuidorVO> cotaCurvaDistribuidor);
}
