package br.com.abril.nds.dto;

import br.com.abril.nds.model.seguranca.Permissao;

/**
 * Classe referente aos menus da lista de menus
 * @author InfoA2
 */
public class MenuDTO {

	private String url;
	
	private Permissao permissao;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}
	
}
