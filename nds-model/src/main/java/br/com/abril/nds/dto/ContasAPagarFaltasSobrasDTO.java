package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ContasAPagarFaltasSobrasDTO implements Serializable {

	private static final long serialVersionUID = -3228847910396394012L;
	
	private String codigo;
	private String produto;
	private String edicao;
	private BigDecimal precoCapa;
	private BigDecimal precoComDesconto;
	private Integer box;
	private Integer exemplares;
	private String fornecedor;
	private BigDecimal valor;
	
	
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
	public String getEdicao() {
		return edicao;
	}
	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}
	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}
	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}
	public Integer getBox() {
		return box;
	}
	public void setBox(Integer box) {
		this.box = box;
	}
	public Integer getExemplares() {
		return exemplares;
	}
	public void setExemplares(Integer exemplares) {
		this.exemplares = exemplares;
	}
	public String getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
