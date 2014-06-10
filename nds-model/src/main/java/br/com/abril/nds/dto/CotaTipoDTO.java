package br.com.abril.nds.dto;

import java.io.Serializable;

public class CotaTipoDTO implements Serializable {

	private static final long serialVersionUID = 171459604717612572L;

	private Long idCota;
	private Integer numCota;
	private String nome;
	private String municipio;
	private String endereco;
	private Boolean selecionado;
	
	public CotaTipoDTO() {
		
	}
	
	public CotaTipoDTO(Long idCota, Integer numCota, String nome, String municipio,
			String endereco, Boolean selecionado) {
		super();
		this.idCota = idCota;
		this.numCota = numCota;
		this.nome = nome;
		this.municipio = municipio;
		this.endereco = endereco;
		this.selecionado = selecionado;
	}
	/**
	 * @return the numCota
	 */
	public Integer getNumCota() {
		return numCota;
	}
	/**
	 * @param numCota the numCota to set
	 */
	public void setNumCota(Integer numCota) {
		this.numCota = numCota;
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
	 * @return the municipio
	 */
	public String getMunicipio() {
		return municipio;
	}
	/**
	 * @param municipio the municipio to set
	 */
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	/**
	 * @return the endereco
	 */
	public String getEndereco() {
		return endereco;
	}
	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	/**
	 * @return the idCota
	 */
	public Long getIdCota() {
		return idCota;
	}

	/**
	 * @param idCota the idCota to set
	 */
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
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

	
}
