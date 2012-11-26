package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RelatorioTiposProdutosDTO implements Serializable {

	private static final long serialVersionUID = -9000972218243942262L;
	
	private String codigo;
	private String produto;
	private Long edicao;
	private BigDecimal precoCapa;
	private BigDecimal faturamento;
	private String tipoProduto;
	private Date lancamento;
	private Date recolhimento;
	
	
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
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}
	public BigDecimal getFaturamento() {
		if(faturamento == null)
			return BigDecimal.ZERO;
		return faturamento;
	}
	public void setFaturamento(BigDecimal faturamento) {
		this.faturamento = (BigDecimal) faturamento;
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
