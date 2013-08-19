package br.com.abril.nds.client.endereco.vo;

import java.io.Serializable;

public class LocalidadeVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String abreviatura;
	private String cep;

	public LocalidadeVO(String nome, String abreviatura, String cep) {
		this.nome = nome;
		this.abreviatura = abreviatura;
		this.cep = cep;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getAbreviatura() {
		return abreviatura;
	}
	
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
	
	public String getCep() {
		return cep;
	}
	
	public void setCep(String cep) {
		this.cep = cep;
	}

}
