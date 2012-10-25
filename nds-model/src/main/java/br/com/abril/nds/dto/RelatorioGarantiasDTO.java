package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class RelatorioGarantiasDTO implements Serializable{

	private static final long serialVersionUID = -9219157726900444816L;
	
	private String tipoGarantia;
	private Integer qtdCotas;
	private BigDecimal vlrTotal;
	
	public String getTipoGarantia() {
		return tipoGarantia;
	}
	public void setTipoGarantia(String tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}
	public Integer getQtdCotas() {
		return qtdCotas;
	}
	public void setQtdCotas(Integer qtdCotas) {
		this.qtdCotas = qtdCotas;
	}
	public BigDecimal getVlrTotal() {
		return vlrTotal;
	}
	public void setVlrTotal(BigDecimal vlrTotal) {
		this.vlrTotal = vlrTotal;
	}
	
	

}
