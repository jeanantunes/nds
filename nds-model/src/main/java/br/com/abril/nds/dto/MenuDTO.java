package br.com.abril.nds.dto;

import br.com.abril.nds.model.seguranca.Permissao;

/**
 * Classe referente aos menus da lista de menus
 * @author InfoA2
 */
public class MenuDTO implements Comparable<MenuDTO> {

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

	public MenuDTO(Permissao permissao, String url) {
		this.permissao = permissao;
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MenuDTO outroMenuDTO) {
		return this.getPermissao().compareTo(outroMenuDTO.getPermissao());
	}

	@Override
	public String toString() {
		return this.getPermissao().getDescricao() + " " + this.getUrl();
	}
	
}
