package br.com.abril.nds.dto;

import java.io.Serializable;

public class RotaRoteiroDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3210097887029811712L;

	private Long idRota;

	private String descricaoRota;

	private String descricaoRoteiro;
	
	private boolean disponivel = true;
	
	public RotaRoteiroDTO(){}

	public RotaRoteiroDTO(Long idRota, String descricaoRota,
			String descricaoRoteiro) {

		this.idRota = idRota;
		this.descricaoRota = descricaoRota;
		this.descricaoRoteiro = descricaoRoteiro;
	}

	public Long getIdRota() {
		return idRota;
	}

	public void setIdRota(Long idRota) {
		this.idRota = idRota;
	}

	public String getDescricaoRota() {
		return descricaoRota;
	}

	public void setDescricaoRota(String descricaoRota) {
		this.descricaoRota = descricaoRota;
	}

	public String getDescricaoRoteiro() {
		return descricaoRoteiro;
	}

	public void setDescricaoRoteiro(String descricaoRoteiro) {
		this.descricaoRoteiro = descricaoRoteiro;
	}

	public boolean isDisponivel() {
		return disponivel;
	}

	public void setDisponivel(boolean disponivel) {
		this.disponivel = disponivel;
	}
}