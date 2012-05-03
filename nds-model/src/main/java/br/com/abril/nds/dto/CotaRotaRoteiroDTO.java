package br.com.abril.nds.dto;

import java.io.Serializable;

/**
 * 
 * @author Diego Fernandes
 *
 */
public class CotaRotaRoteiroDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6933180219261652814L;
	

	private Integer numeroCota;
	private String nomeCota;	
	private String rota;
	private String roteiro;
	
	
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	public String getRota() {
		return rota;
	}
	public void setRota(String rota) {
		this.rota = rota;
	}
	public String getRoteiro() {
		return roteiro;
	}
	public void setRoteiro(String roteiro) {
		this.roteiro = roteiro;
	}
	
}
