package br.com.abril.nds.client.endereco.vo;

import java.io.Serializable;

public class PaisVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sigla;
	private String nome;
	
	public PaisVO(String sigla, String nome) {
		this.sigla = sigla;
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

}
