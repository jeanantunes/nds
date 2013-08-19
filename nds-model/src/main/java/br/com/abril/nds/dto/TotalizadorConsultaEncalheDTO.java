package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * DTO que representa os totais referentes à consulta de encalhe. 
 * 
 * @author Discover Technology
 *
 */
public class TotalizadorConsultaEncalheDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8252580930195247568L;

	private BigDecimal valorReparte;

	private BigDecimal valorEncalhe;
	
	private BigInteger qtdConsultaEncalhe;

	/**
	 * @return the valorReparte
	 */
	public BigDecimal getValorReparte() {
		return valorReparte;
	}

	/**
	 * @param valorReparte the valorReparte to set
	 */
	public void setValorReparte(BigDecimal valorReparte) {
		this.valorReparte = valorReparte;
	}

	/**
	 * @return the valorEncalhe
	 */
	public BigDecimal getValorEncalhe() {
		return valorEncalhe;
	}

	/**
	 * @param valorEncalhe the valorEncalhe to set
	 */
	public void setValorEncalhe(BigDecimal valorEncalhe) {
		this.valorEncalhe = valorEncalhe;
	}

	/**
	 * @return the qtdConsultaEncalhe
	 */
	public BigInteger getQtdConsultaEncalhe() {
		return qtdConsultaEncalhe;
	}

	/**
	 * @param qtdConsultaEncalhe the qtdConsultaEncalhe to set
	 */
	public void setQtdConsultaEncalhe(BigInteger qtdConsultaEncalhe) {
		this.qtdConsultaEncalhe = qtdConsultaEncalhe == null ? BigInteger.ZERO : qtdConsultaEncalhe;
	}
}
