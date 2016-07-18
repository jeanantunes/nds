package br.com.abril.ndsled.modelo;

import java.math.BigDecimal;

public class Produto {
	private Integer codigoProduto;
	private Integer edicaoProduto;
	private String nomeProduto;
	private BigDecimal precoCusto;
	private BigDecimal precoCapa;
	private BigDecimal desconto;

	public Integer getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(Integer codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public Integer getEdicaoProduto() {
		return edicaoProduto;
	}

	public void setEdicaoProduto(Integer edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public BigDecimal getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(BigDecimal precoCusto) {
		this.precoCusto = precoCusto;
	}

	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	@Override
	public String toString() {
		return codigoProduto + " - " + edicaoProduto + " - " + nomeProduto;
	}

}
