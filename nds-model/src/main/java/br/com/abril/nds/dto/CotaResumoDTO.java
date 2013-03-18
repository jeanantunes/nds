package br.com.abril.nds.dto;

import java.io.Serializable;

public class CotaResumoDTO implements Serializable {

	private static final long serialVersionUID = 5903684047811451940L;

	private String nome;
	private Integer numero;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	


}
