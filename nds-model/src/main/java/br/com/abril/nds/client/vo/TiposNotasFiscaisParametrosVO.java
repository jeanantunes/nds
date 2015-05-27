package br.com.abril.nds.client.vo;

import java.io.Serializable;

public class TiposNotasFiscaisParametrosVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2363370368181215212L;

	private String nome;
	
	private String valor;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}