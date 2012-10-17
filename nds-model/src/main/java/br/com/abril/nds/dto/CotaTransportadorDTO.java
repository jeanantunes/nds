package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CotaTransportadorDTO implements Serializable {

	private static final long serialVersionUID = -1022837911088999851L;

	private String transportador;
	private String roteiro;
	private String rota;
	private Integer numCota;
	private String nomeCota;
	private BigDecimal valor;
	
	private Long idTransportador;
	private Long idCota;
	
	public CotaTransportadorDTO() {
		
	}
		
	public CotaTransportadorDTO(String transportador, String roteiro,
			String rota, Integer numCota, String nomeCota, BigDecimal valor,
			Long idTransportador, Long idCota) {
		super();
		this.transportador = transportador;
		this.roteiro = roteiro;
		this.rota = rota;
		this.numCota = numCota;
		this.nomeCota = nomeCota;
		this.valor = valor;
		this.idTransportador = idTransportador;
		this.idCota = idCota;
	}

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
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public Long getIdTransportador() {
		return idTransportador;
	}
	public void setIdTransportador(Long idTransportador) {
		this.idTransportador = idTransportador;
	}
	public Long getIdCota() {
		return idCota;
	}
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}
		
	
}
