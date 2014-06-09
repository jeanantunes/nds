package br.com.abril.nds.dto;

import java.math.BigDecimal;

/**
 * DTO que encapsula os valores de agregação referentes à entidade {@link InfoContagemDevolucaoDTO}
 * 
 * @author Discover Technology
 *
 */
public class ContagemDevolucaoAgregationValuesDTO {
	
	private Integer quantidadeTotal;
	
	private BigDecimal valorTotalGeral;

	/**
	 * @return the quantidadeTotal
	 */
	public Integer getQuantidadeTotal() {
		return quantidadeTotal;
	}

	/**
	 * @param quantidadeTotal the quantidadeTotal to set
	 */
	public void setQuantidadeTotal(Integer quantidadeTotal) {
		this.quantidadeTotal = quantidadeTotal;
	}

	/**
	 * @return the valorTotalGeral
	 */
	public BigDecimal getValorTotalGeral() {
		return valorTotalGeral;
	}

	/**
	 * @param valorTotalGeral the valorTotalGeral to set
	 */
	public void setValorTotalGeral(BigDecimal valorTotalGeral) {
		this.valorTotalGeral = valorTotalGeral;
	}
}
