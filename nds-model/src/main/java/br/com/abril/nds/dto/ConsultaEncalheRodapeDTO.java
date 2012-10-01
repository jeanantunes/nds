package br.com.abril.nds.dto;

import java.math.BigDecimal;

public class ConsultaEncalheRodapeDTO {

	private BigDecimal valorReparte;

	private BigDecimal valorEncalhe;

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
	
}
