package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.ResultadoPermissaoVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaPermissaoDTO;

/**
 * @author InfoA2
 */
public interface PermissaoService {

	public List<ResultadoPermissaoVO> buscar(FiltroConsultaPermissaoDTO filtro);
	
}
