package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PagamentoDTO {

	private String nossoNumero;
	
	private Date dataPagamento;
	
	private BigDecimal valorPagamento;
	
	private BigDecimal taxaBancaria;
	
	private BigDecimal valorMulta;
	
	private BigDecimal valorJuros;
	
	private BigDecimal valorDesconto;
	
	private Long numeroRegistro;

	/**
	 * @return the nossoNumero
	 */
	public String getNossoNumero() {
		return nossoNumero;
	}

	/**
	 * @param nossoNumero the nossoNumero to set
	 */
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	/**
	 * @return the dataPagamento
	 */
	public Date getDataPagamento() {
		return dataPagamento;
	}

	/**
	 * @param dataPagamento the dataPagamento to set
	 */
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	/**
	 * @return the valorPagamento
	 */
	public BigDecimal getValorPagamento() {
		return valorPagamento;
	}

	/**
	 * @param valorPagamento the valorPagamento to set
	 */
	public void setValorPagamento(BigDecimal valorPagamento) {
		this.valorPagamento = valorPagamento;
	}
	
	/**
	 * @return the valorMulta
	 */
	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	/**
	 * @param valorMulta the valorMulta to set
	 */
	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	/**
	 * @return the valorJuros
	 */
	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	/**
	 * @param valorJuros the valorJuros to set
	 */
	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}
	
	/**
	 * @return the valorDesconto
	 */
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	/**
	 * @param valorDesconto the valorDesconto to set
	 */
	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}
	
	/**
	 * @return the numeroRegistro
	 */
	public Long getNumeroRegistro() {
		return numeroRegistro;
	}

	/**
	 * @param numeroRegistro the numeroRegistro to set
	 */
	public void setNumeroRegistro(Long numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public BigDecimal getTaxaBancaria() {
		return taxaBancaria;
	}

	public void setTaxaBancaria(BigDecimal taxaBancaria) {
		this.taxaBancaria = taxaBancaria;
	}
}