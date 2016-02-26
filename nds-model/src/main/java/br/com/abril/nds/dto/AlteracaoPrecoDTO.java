package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class AlteracaoPrecoDTO implements Serializable {
	
	
	private static final long serialVersionUID = -9160921967560123153L;

	private String codigo;
	
	private BigInteger numeroEdicao;

	private BigDecimal valorAntigo;
	
	private BigDecimal valorAtual;
	
	private String nomeUsuario;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public BigInteger getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(BigInteger numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigDecimal getValorAntigo() {
		return valorAntigo;
	}

	public void setValorAntigo(BigDecimal valorAntigo) {
		this.valorAntigo = valorAntigo;
	}

	public BigDecimal getValorAtual() {
		return valorAtual;
	}

	public void setValorAtual(BigDecimal valorAtual) {
		this.valorAtual = valorAtual;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
}