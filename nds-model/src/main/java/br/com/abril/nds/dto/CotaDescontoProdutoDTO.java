package br.com.abril.nds.dto;

import java.io.Serializable;

public class CotaDescontoProdutoDTO implements Serializable {

	private static final long serialVersionUID = 795606015671735022L;

	private Integer numeroCota;
	
	private String nome;

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
}
