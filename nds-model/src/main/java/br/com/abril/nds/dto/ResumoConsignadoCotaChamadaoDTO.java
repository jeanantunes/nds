package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ResumoConsignadoCotaChamadaoDTO implements Serializable{
	
	private static final long serialVersionUID = 5652914511760044649L;
	
	private Long qtdProdutosTotal;
	
	private BigDecimal qtdExemplaresTotal;
	
	private BigDecimal valorTotal;

	/**
	 * @return the qtdProdutosTotal
	 */
	public Long getQtdProdutosTotal() {
		return qtdProdutosTotal;
	}

	/**
	 * @param qtdProdutosTotal the qtdProdutosTotal to set
	 */
	public void setQtdProdutosTotal(Long qtdProdutosTotal) {
		this.qtdProdutosTotal = qtdProdutosTotal;
	}

	/**
	 * @return the qtdExemplaresTotal
	 */
	public BigDecimal getQtdExemplaresTotal() {
		return qtdExemplaresTotal;
	}

	/**
	 * @param qtdExemplaresTotal the qtdExemplaresTotal to set
	 */
	public void setQtdExemplaresTotal(BigDecimal qtdExemplaresTotal) {
		this.qtdExemplaresTotal = qtdExemplaresTotal;
	}

	/**
	 * @return the valorTotal
	 */
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

}
