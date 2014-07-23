package br.com.abril.nds.dto;

import java.io.Serializable;

public class ComboTipoFornecedorDTO implements Serializable {

	private static final long serialVersionUID = -9082711648975240577L;

	private Long id;
	
	private String descricao;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
