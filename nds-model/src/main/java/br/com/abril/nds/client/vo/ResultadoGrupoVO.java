package br.com.abril.nds.client.vo;

import java.io.Serializable;

/**
 * @author InfoA2
 */
public class ResultadoGrupoVO implements Serializable {
	
	private static final long serialVersionUID = -3283513386337622368L;

	private String nome;

	private Long id;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
