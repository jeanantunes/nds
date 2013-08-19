package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.ResultadoGrupoVO;
import br.com.abril.nds.dto.GrupoPermissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaGrupoDTO;
import br.com.abril.nds.model.seguranca.GrupoPermissao;

/**
 * @author InfoA2
 */
public interface GrupoPermissaoService {

	public void salvar(GrupoPermissao grupoPermissao);
	
	public GrupoPermissao buscar(Long codigo);

	public List<ResultadoGrupoVO> listar(FiltroConsultaGrupoDTO filtro);

	public List<GrupoPermissaoDTO> listarDTOs();

	public void excluir(Long codigoGrupo);

}
