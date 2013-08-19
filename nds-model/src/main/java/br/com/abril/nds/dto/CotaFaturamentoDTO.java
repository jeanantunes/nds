package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CotaFaturamentoDTO implements Serializable {

	/**
	 * serialVersionUID.
	*/
	private static final long serialVersionUID = 1213654878455151422L;
	
	private Long idCota;
	private BigDecimal faturamentoLiquido;
	private BigDecimal faturamentoBruto;
	
	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public BigDecimal getFaturamentoLiquido() {
		return faturamentoLiquido;
	}
	
	public void setFaturamentoLiquido(BigDecimal faturamentoLiquido) {
		this.faturamentoLiquido = faturamentoLiquido;
	}
	
	public BigDecimal getFaturamentoBruto() {
		return faturamentoBruto;
	}
	
	public void setFaturamentoBruto(BigDecimal faturamentoBruto) {
		this.faturamentoBruto = faturamentoBruto;
	}
	
}
