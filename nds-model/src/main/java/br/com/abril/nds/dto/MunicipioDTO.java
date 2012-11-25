package br.com.abril.nds.dto;

import java.io.Serializable;

public class MunicipioDTO  implements Serializable {
	 
	private static final long serialVersionUID = 1753803641359682149L;

	private String municipio;
	private Integer qtde;
	private Boolean selecionado;
	
	public MunicipioDTO() {
		
	}
	
	public MunicipioDTO(String municipio, Integer qtde, Boolean selecionado) {
		super();
		this.municipio = municipio;
		this.qtde = qtde;
		this.selecionado = selecionado;
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
	 * @return the qtde
	 */
	public Integer getQtde() {
		return qtde;
	}
	/**
	 * @param qtde the qtde to set
	 */
	public void setQtde(Long qtde) {
		this.qtde = qtde.intValue();
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
	 * @return the codigoMunicipioIBGE
	 */	
		
	
}
