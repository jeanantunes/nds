package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/*
 * Classe utilizada no detalhamento do produto. 
 * 
 * Funcionalidade: Reparte Total e Promocional do Field Reparte da edição.
 * 
 */

public class InformacoesReparteTotalEPromocionalDTO implements Serializable {

	private static final long serialVersionUID = 6561927362578562028L;
	
	private BigDecimal reparteTotal;
	private BigDecimal repartePromocional;

	public BigDecimal getReparteTotal() {
		return reparteTotal;
	}
	public void setReparteTotal(BigDecimal reparteTotal) {
		this.reparteTotal = reparteTotal;
	}
	public BigDecimal getRepartePromocional() {
		return repartePromocional;
	}
	public void setRepartePromocional(BigDecimal repartePromocional) {
		this.repartePromocional = repartePromocional;
	}
}
