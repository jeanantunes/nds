package br.com.abril.nds.dto;

import java.io.Serializable;

public class RateioDTO implements Serializable {

	private static final long serialVersionUID = -1771002096802812477L;

	private Integer numCota;
	private String nomeCota;
	private Integer qtde;
	
	
	public RateioDTO() {
		
	}
	
	public RateioDTO(Integer numCota, String nomeCota, Integer qtde) {
		super();
		this.numCota = numCota;
		this.nomeCota = nomeCota;
		this.qtde = qtde;
	}
	
	public Integer getNumCota() {
		return numCota;
	}
	public void setNumCota(Integer numCota) {
		this.numCota = numCota;
	}
	public Integer getQtde() {
		return qtde;
	}
	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
}
