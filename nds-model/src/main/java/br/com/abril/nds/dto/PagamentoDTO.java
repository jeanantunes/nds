package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PagamentoDTO {

	private String nossoNumero;
	
	private Date dataPagamento;
	
	private BigDecimal valorPagamento;

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
	
}
