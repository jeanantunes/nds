package br.com.abril.nds.dto.auditoria;

import java.io.Serializable;

public class AtributoDTO implements Serializable {

	private static final long serialVersionUID = -6207168173587808947L;

	private String nome;
	
	private Object valor;

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

	/**
	 * @return the valor
	 */
	public Object getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(Object valor) {
		this.valor = valor;
	}
}
