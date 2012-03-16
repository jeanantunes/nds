package br.com.abril.nds.client.endereco.vo;

import java.io.Serializable;

public class TipoLogradouroVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String abreviacao;
	
	public TipoLogradouroVO(String nome, String abreviacao) {
		this.nome = nome;
		this.abreviacao = abreviacao;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getAbreviacao() {
		return abreviacao;
	}
	
	public void setAbreviacao(String abreviacao) {
		this.abreviacao = abreviacao;
	}

}
