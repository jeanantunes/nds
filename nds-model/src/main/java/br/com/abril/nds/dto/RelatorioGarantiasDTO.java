package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.model.cadastro.TipoGarantia;

public class RelatorioGarantiasDTO implements Serializable{

	private static final long serialVersionUID = -9219157726900444816L;
	
	private String tipoGarantia;
	private Integer qtdCotas;
	private BigDecimal vlrTotal;
	private TipoGarantia tipoGarantiaEnum;
	
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
	public TipoGarantia getTipoGarantiaEnum() {
		return tipoGarantiaEnum;
	}
	public void setTipoGarantiaEnum(TipoGarantia tipoGarantiaEnum) {
		this.tipoGarantiaEnum = tipoGarantiaEnum;
	}
	
	

}
