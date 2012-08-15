package br.com.abril.nds.dto;

import java.io.Serializable;

/**
 * @author InfoA2
 */
public class GrupoPermissaoDTO implements Serializable {

	private static final long serialVersionUID = 97687723335454041L;

	private Long id;
	
	private String nome;

	private String permissoesSelecionadas;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPermissoesSelecionadas() {
		return permissoesSelecionadas;
	}

	public void setPermissoesSelecionadas(String permissoesSelecionadas) {
		this.permissoesSelecionadas = permissoesSelecionadas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GrupoPermissaoDTO other = (GrupoPermissaoDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	
}
