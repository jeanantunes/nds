package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CotaFuroDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String numeroCota;
	
	private String nome;
	
	private String nossoNumero;

	private BigDecimal valor;
	
	public String getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(String numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}