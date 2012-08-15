package br.com.abril.nds.dto;

import java.util.List;

import br.com.abril.nds.model.seguranca.Permissao;

/**
 * @author InfoA2
 */
public class ConsultaGrupoPermissaoDTO {

	private String nome;

	private Long id;
	
	private List<Permissao> permissoes;

	private List<Permissao> permissoesSelecionadas;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public List<Permissao> getPermissoesSelecionadas() {
		return permissoesSelecionadas;
	}

	public void setPermissoesSelecionadas(List<Permissao> permissoesSelecionadas) {
		this.permissoesSelecionadas = permissoesSelecionadas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
