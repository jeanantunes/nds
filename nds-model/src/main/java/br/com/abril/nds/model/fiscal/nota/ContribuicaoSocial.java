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
	
	protected String cst;
	
	protected BigDecimal valorBaseCalculoDebito;
	
	protected BigDecimal valorBaseCalculoCredito;
	
	protected BigDecimal aliquota;
	
	protected BigDecimal valorDebito;
	
	protected BigDecimal valorCredito;

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
	 * @return the valorBaseCalculoDebito
	 */
	public BigDecimal getValorBaseCalculoDebito() {
		return valorBaseCalculoDebito;
	}

	/**
	 * @param valorBaseCalculoDebito the valorBaseCalculoDebito to set
	 */
	public void setValorBaseCalculoDebito(BigDecimal valorBaseCalculoDebito) {
		this.valorBaseCalculoDebito = valorBaseCalculoDebito;
	}

	/**
	 * @return the valorBaseCalculoCredito
	 */
	public BigDecimal getValorBaseCalculoCredito() {
		return valorBaseCalculoCredito;
	}

	/**
	 * @param valorBaseCalculoCredito the valorBaseCalculoCredito to set
	 */
	public void setValorBaseCalculoCredito(BigDecimal valorBaseCalculoCredito) {
		this.valorBaseCalculoCredito = valorBaseCalculoCredito;
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

	/**
	 * @return the valorDebito
	 */
	public BigDecimal getValorDebito() {
		return valorDebito;
	}

	/**
	 * @param valorDebito the valorDebito to set
	 */
	public void setValorDebito(BigDecimal valorDebito) {
		this.valorDebito = valorDebito;
	}

	/**
	 * @return the valorCredito
	 */
	public BigDecimal getValorCredito() {
		return valorCredito;
	}

	/**
	 * @param valorCredito the valorCredito to set
	 */
	public void setValorCredito(BigDecimal valorCredito) {
		this.valorCredito = valorCredito;
	}

}
