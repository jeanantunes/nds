package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProcessamentoFinanceiroCotaDTO implements Serializable {

	private static final long serialVersionUID = 112346987L;
	
	private Integer numeroCota;
	
	private String nomeCota;
	
	private BigDecimal valorConsignado;
	
	private BigDecimal valorPendenteDebito;
	
	private BigDecimal valorPendenteCredito;
	
	private BigDecimal valorAVista;
	
	private BigDecimal valorEstornado;
	
	private BigDecimal debitos;
	
	private BigDecimal creditos;
	
	private BigDecimal saldo;

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

	public BigDecimal getValorConsignado() {
		return valorConsignado;
	}

	public void setValorConsignado(BigDecimal valorConsignado) {
		this.valorConsignado = valorConsignado;
	}
	
	public BigDecimal getValorPendenteDebito() {
		return valorPendenteDebito;
	}

	public void setValorPendenteDebito(BigDecimal valorPendenteDebito) {
		this.valorPendenteDebito = valorPendenteDebito;
	}

	public BigDecimal getValorPendenteCredito() {
		return valorPendenteCredito;
	}

	public void setValorPendenteCredito(BigDecimal valorPendenteCredito) {
		this.valorPendenteCredito = valorPendenteCredito;
	}

	public BigDecimal getValorAVista() {
		return valorAVista;
	}

	public void setValorAVista(BigDecimal valorAVista) {
		this.valorAVista = valorAVista;
	}

	public BigDecimal getValorEstornado() {
		return valorEstornado;
	}

	public void setValorEstornado(BigDecimal valorEstornado) {
		this.valorEstornado = valorEstornado;
	}

	public BigDecimal getDebitos() {
		return debitos;
	}

	public void setDebitos(BigDecimal debitos) {
		this.debitos = debitos;
	}

	public BigDecimal getCreditos() {
		return creditos;
	}

	public void setCreditos(BigDecimal creditos) {
		this.creditos = creditos;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
}
