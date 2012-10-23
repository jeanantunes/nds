package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class RelatorioServicosEntregaDTO implements Serializable {
	
	private static final long serialVersionUID = -7333083999971377674L;
	
	private String nomeTransportador;
	private String descricaoRoteiro;
	private String descricaoRota;
	private Integer numeroCota;
	private String nomeJornaleiro;
	private BigDecimal valor;
	
	
	public String getNomeTransportador() {
		return nomeTransportador;
	}
	public void setNomeTransportador(String nomeTransportador) {
		this.nomeTransportador = nomeTransportador;
	}
	public String getDescricaoRoteiro() {
		return descricaoRoteiro;
	}
	public void setDescricaoRoteiro(String descricaoRoteiro) {
		this.descricaoRoteiro = descricaoRoteiro;
	}
	public String getDescricaoRota() {
		return descricaoRota;
	}
	public void setDescricaoRota(String descricaoRota) {
		this.descricaoRota = descricaoRota;
	}
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	public String getNomeJornaleiro() {
		return nomeJornaleiro;
	}
	public void setNomeJornaleiro(String nomeJornaleiro) {
		this.nomeJornaleiro = nomeJornaleiro;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
