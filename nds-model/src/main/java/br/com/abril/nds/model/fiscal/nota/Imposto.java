package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Imposto implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 536918061402194564L;

	protected String cst;
	
	protected BigDecimal valor;
	
	protected BigDecimal valorBaseCalculo;
	
	protected BigDecimal aliquota;

	/**
	 * @return the cst
	 */
	public String getCst() {
		return cst;
	}

	/**
	 * @param cst the cst to set
	 */
	public void setCst(String cst) {
		this.cst = cst;
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
	 * @return the aliquota
	 */
	public BigDecimal getAliquota() {
		return aliquota;
	}

	/**
	 * @param aliquota the aliquota to set
	 */
	public void setAliquota(BigDecimal aliquota) {
		this.aliquota = aliquota;
	}
	
}
