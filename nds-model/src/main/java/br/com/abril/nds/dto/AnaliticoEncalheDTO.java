package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.model.financeiro.StatusDivida;

public class AnaliticoEncalheDTO implements Serializable {

	private static final long serialVersionUID = -2784902117985504916L;

	private Integer numeroCota;
	private String nomeCota;
	private String boxEncalhe;
	private BigDecimal total;
	private StatusDivida statusCobranca;
	
	
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
	public String getBoxEncalhe() {
		return boxEncalhe;
	}
	public void setBoxEncalhe(String boxEncalhe) {
		this.boxEncalhe = boxEncalhe;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public StatusDivida getStatusCobranca() {
		return statusCobranca;
	}
	public void setStatusCobranca(StatusDivida statusCobranca) {
		this.statusCobranca = statusCobranca;
	}
}
