package br.com.abril.nds.client.endereco.vo;

import java.io.Serializable;

public class BairroVO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String nome;
	private String abreviatura;
	
	public BairroVO(String nome, String abreviatura) {
		this.nome = nome;
		this.abreviatura = abreviatura;
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

}
