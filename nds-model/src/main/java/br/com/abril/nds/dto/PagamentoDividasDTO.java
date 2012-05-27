package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoCobranca;

public class PagamentoDividasDTO {

	private Date dataPagamento;
	
	private BigDecimal valorDividas;
	
	private BigDecimal valorMulta;
	
	private BigDecimal valorJuros;
	
	private BigDecimal valorDesconto;
	
	private BigDecimal valorSaldo;
	
	private BigDecimal valorPagamento;
	
	private TipoCobranca tipoPagamento;
	
	private String observacoes;

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getValorDividas() {
		return valorDividas;
	}

	public void setValorDividas(BigDecimal valorDividas) {
		this.valorDividas = valorDividas;
	}

	public BigDecimal getValorPagamento() {
		return valorPagamento;
	}

	public void setValorPagamento(BigDecimal valorPagamento) {
		this.valorPagamento = valorPagamento;
	}

	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorSaldo() {
		return valorSaldo;
	}

	public void setValorSaldo(BigDecimal valorSaldo) {
		this.valorSaldo = valorSaldo;
	}

	public TipoCobranca getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoCobranca tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	
}
