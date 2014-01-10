package br.com.abril.nds.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.dto.RegistroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;

/**
 * Interface de serviços de curvas de relatórios de vendas
 * @author InfoA2
 */

public interface RelatorioVendasService {

	List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO);

	List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO);
	
	List<RegistroCurvaABCDistribuidorVO> obterCurvaABCProduto(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO);
	
	List<RegistroCurvaABCCotaDTO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtroCurvaABCCotaDTO);

	Integer obterQtdeRegistrosCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO);

	Integer obterQtdeRegistrosCurvaABCEditor(FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO);
	
	Integer obterQtdeRegistrosCurvaABCCota(FiltroCurvaABCCotaDTO filtroCurvaABCCotaDTO);
	
}
