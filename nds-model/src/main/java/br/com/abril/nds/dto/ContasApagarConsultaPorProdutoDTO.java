package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ContasApagarConsultaPorProdutoDTO implements Serializable{
	
	private static final long serialVersionUID = 3890007339807389818L;
	
	private Date rctl;
	private String codigo;
	private String produto;
	private Long edicao;
	private boolean tipo;
	private BigInteger reparte;
	private BigInteger suplementacao;
	private BigInteger encalhe;
	private BigInteger faltasSobras;
	private BigDecimal debitosCreditos;
	
	
	public Date getRctl() {
		return rctl;
	}
	public void setRctl(Date rctl) {
		this.rctl = rctl;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getProduto() {
		return produto;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	public boolean isTipo() {
		return tipo;
	}
	public void setTipo(boolean tipo) {
		this.tipo = tipo;
	}
	public BigInteger getReparte() {
		return reparte;
	}
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}
	public BigInteger getSuplementacao() {
		return suplementacao;
	}
	public void setSuplementacao(BigInteger suplementacao) {
		this.suplementacao = suplementacao;
	}
	public BigInteger getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(BigInteger encalhe) {
		this.encalhe = encalhe;
	}
	public BigInteger getFaltasSobras() {
		return faltasSobras;
	}
	public void setFaltasSobras(BigInteger faltasSobras) {
		this.faltasSobras = faltasSobras;
	}
	public BigDecimal getDebitosCreditos() {
		return debitosCreditos;
	}
	public void setDebitosCreditos(BigDecimal debitosCreditos) {
		this.debitosCreditos = debitosCreditos;
	}
	
	
	
}
