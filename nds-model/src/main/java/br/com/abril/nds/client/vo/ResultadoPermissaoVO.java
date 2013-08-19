package br.com.abril.nds.client.vo;

import java.io.Serializable;

public class ResultadoPermissaoVO implements Serializable {

	private static final long serialVersionUID = -2414223077228250384L;

	private String nome;
	private String descricao;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
