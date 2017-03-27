package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.financeiro.StatusDivida;

public class AnaliticoEncalheDTO implements Serializable {

	private static final long serialVersionUID = -2784902117985504916L;

	private Integer numeroCota;
	private String nomeCota;
	private String boxEncalhe;
	private BigDecimal total;
	private StatusDivida statusCobranca;
	private String usuario;
	private Date inicio;
	private Date fim;
	private Long id;
	private BigDecimal valorEncalhe;
	
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
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public Date getIncio() {
		return inicio;
	}
	
	public void setIncio(Date inicio) {
		this.inicio = inicio;
	}
	
	public Date getFim() {
		return fim;
	}
	
	public void setFim(Date fim) {
		this.fim = fim;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValorEncalhe() {
		return valorEncalhe;
	}

	public void setValorEncalhe(BigDecimal valorEncalhe) {
		this.valorEncalhe = valorEncalhe;
	}
}