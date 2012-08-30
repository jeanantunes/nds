package br.com.abril.nds.dto;

import java.io.Serializable;

public class BoxRoteirizacaoDTO implements Serializable {

	private static final long serialVersionUID = 6862359582349796624L;

	private Long id;
	
	private Integer ordem;
	
	private String nome;
	
	private Boolean selecionado;

	
	public BoxRoteirizacaoDTO() {
		
	}
	
	public BoxRoteirizacaoDTO(Long id, Integer ordem, String nome,
			Boolean selecionado) {
		super();
		this.id = id;
		this.ordem = ordem;
		this.nome = nome;
		this.selecionado = selecionado;
	}

	/**
	 * @return the ordem
	 */
	public Integer getOrdem() {
		return ordem;
	}

	/**
	 * @param ordem the ordem to set
	 */
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
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

	/**
	 * @return the selecionado
	 */
	public Boolean getSelecionado() {
		return selecionado;
	}

	/**
	 * @param selecionado the selecionado to set
	 */
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

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
	
	
}
