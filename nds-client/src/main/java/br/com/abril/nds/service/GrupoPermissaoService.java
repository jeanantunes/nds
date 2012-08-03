package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.seguranca.GrupoPermissao;
import br.com.abril.nds.model.seguranca.Permissao;

/**
 * @author InfoA2
 */
public interface GrupoPermissaoService {

	public void salvar(GrupoPermissao grupoPermissao);
	
	public List<Permissao> buscarPermissoesGrupo(Long codigo);

}
