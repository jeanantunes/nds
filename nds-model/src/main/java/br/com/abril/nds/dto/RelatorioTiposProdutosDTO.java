package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class RelatorioTiposProdutosDTO implements Serializable {

	private static final long serialVersionUID = -9000972218243942262L;
	
	private BigInteger codigo;
	private String produto;
	private BigInteger edicao;
	private BigDecimal precoCapa;
	private BigDecimal faturamento;
	private String tipoProduto;
	private Date lancamento;
	private Date recolhimento;
	
	
	public BigInteger getCodigo() {
		return codigo;
	}
	public void setCodigo(BigInteger codigo) {
		this.codigo = codigo;
	}
	public String getProduto() {
		return produto;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public BigInteger getEdicao() {
		return edicao;
	}
	public void setEdicao(BigInteger edicao) {
		this.edicao = edicao;
	}
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}
	public BigDecimal getFaturamento() {
		return faturamento;
	}
	public void setFaturamento(BigDecimal faturamento) {
		this.faturamento = faturamento;
	}
	public String getTipoProduto() {
		return tipoProduto;
	}
	public void setTipoProduto(String tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	public Date getLancamento() {
		return lancamento;
	}
	public void setLancamento(Date lancamento) {
		this.lancamento = lancamento;
	}
	public Date getRecolhimento() {
		return recolhimento;
	}
	public void setRecolhimento(Date recolhimento) {
		this.recolhimento = recolhimento;
	}
}
