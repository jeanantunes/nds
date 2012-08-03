package br.com.abril.nds.service;

import br.com.abril.nds.model.seguranca.GrupoPermissao;

/**
 * @author InfoA2
 */
public interface GrupoPermissaoService {

	public void salvar(GrupoPermissao grupoPermissao);
	
	public GrupoPermissao buscar(Long codigo);

}
