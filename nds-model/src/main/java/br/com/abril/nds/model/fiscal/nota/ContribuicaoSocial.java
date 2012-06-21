package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ContribuicaoSocial implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7720053662091868121L;
	
	protected Integer cst;
	
	protected BigDecimal valorBaseCalculo;
	
	protected BigDecimal percentualAliquota;
	
	protected BigDecimal quantidadeVendida;
	
	protected BigDecimal valorAliquota;
	
	protected BigDecimal valor;

	/**
	 * @return the cst
	 */
	public Integer getCst() {
		return cst;
	}

	/**
	 * @param cst the cst to set
	 */
	public void setCst(Integer cst) {
		this.cst = cst;
	}

	/**
	 * @return the valorBaseCalculo
	 */
	public BigDecimal getValorBaseCalculo() {
		return valorBaseCalculo;
	}

	/**
	 * @param valorBaseCalculo the valorBaseCalculo to set
	 */
	public void setValorBaseCalculo(BigDecimal valorBaseCalculo) {
		this.valorBaseCalculo = valorBaseCalculo;
	}

	/**
	 * @return the percentualAliquota
	 */
	public BigDecimal getPercentualAliquota() {
		return percentualAliquota;
	}

	/**
	 * @param percentualAliquota the percentualAliquota to set
	 */
	public void setPercentualAliquota(BigDecimal percentualAliquota) {
		this.percentualAliquota = percentualAliquota;
	}

	/**
	 * @return the quantidadeVendida
	 */
	public BigDecimal getQuantidadeVendida() {
		return quantidadeVendida;
	}

	/**
	 * @param quantidadeVendida the quantidadeVendida to set
	 */
	public void setQuantidadeVendida(BigDecimal quantidadeVendida) {
		this.quantidadeVendida = quantidadeVendida;
	}

	/**
	 * @return the valorAliquota
	 */
	public BigDecimal getValorAliquota() {
		return valorAliquota;
	}

	/**
	 * @param valorAliquota the valorAliquota to set
	 */
	public void setValorAliquota(BigDecimal valorAliquota) {
		this.valorAliquota = valorAliquota;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	
}
