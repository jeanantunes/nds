package br.com.abril.ndsled.modelo;

import java.math.BigDecimal;
import java.sql.Date;

public class Lancamento {

	private Integer codigoCota;
	private Integer codigoProduto;
	private Integer edicaoProduto;
	private String nomeProduto;
	private BigDecimal precoCusto;
	private BigDecimal precoCapa;
	private BigDecimal desconto;
	private Integer QuantidadeReparte;
	private Date dataLacamento;
	private Integer codigoLed;

	public Integer getCodigoCota() {
		return codigoCota;
	}

	public void setCodigoCota(Integer codigoCota) {
		this.codigoCota = codigoCota;
	}

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

	public Integer getQuantidadeReparte() {
		return QuantidadeReparte;
	}

	public void setQuantidadeReparte(Integer quantidadeReparte) {
		QuantidadeReparte = quantidadeReparte;
	}

	public Date getDataLacamento() {
		return dataLacamento;
	}

	public void setDataLacamento(Date dataLacamento) {
		this.dataLacamento = dataLacamento;
	}

	public Integer getCodigoLed() {
		return codigoLed;
	}

	public void setCodigoLed(Integer codigoLed) {
		this.codigoLed = codigoLed;
	}

	@Override
	public String toString() {
		return codigoProduto + " " + edicaoProduto + " " + nomeProduto + " "
				+ QuantidadeReparte + " " + dataLacamento;
	}

}
