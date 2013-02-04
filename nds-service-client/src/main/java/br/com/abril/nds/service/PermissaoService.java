package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.ResultadoPermissaoVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaPermissaoDTO;
import br.com.abril.nds.model.seguranca.Permissao;

/**
 * @author InfoA2
 */
public interface PermissaoService {

	public List<Permissao> buscar();

	public List<ResultadoPermissaoVO> buscarResultado(FiltroConsultaPermissaoDTO filtro);
	
}
