package br.com.abril.nds.dto;

import java.io.Serializable;

public class CotaTransportadorDTO implements Serializable {

	private static final long serialVersionUID = -1022837911088999851L;

	private String transportador;
	private String roteiro;
	private String rota;
	private Integer numCota;
	private String nomeCota;
	private String valor;
	
	public String getTransportador() {
		return transportador;
	}
	public void setTransportador(String transportador) {
		this.transportador = transportador;
	}
	public String getRoteiro() {
		return roteiro;
	}
	public void setRoteiro(String roteiro) {
		this.roteiro = roteiro;
	}
	public String getRota() {
		return rota;
	}
	public void setRota(String rota) {
		this.rota = rota;
	}
	public Integer getNumCota() {
		return numCota;
	}
	public void setNumCota(Integer numCota) {
		this.numCota = numCota;
	}
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
		
	
}
