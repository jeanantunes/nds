package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class OutraMovimentacaoDTO implements Serializable {
	
	private static final long serialVersionUID = -9160921967560123153L;

	private Integer numeroCota;
	
	private String nomeCota;

	private BigDecimal valor;
	
	private String operacao;

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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
}