package br.com.abril.nds.dto;

import java.math.BigDecimal;

public class FechamentoCEIntegracaoConsolidadoDTO {

	private BigDecimal totalBruto;

	private BigDecimal totalDesconto;

	private BigDecimal totalLiquido;

	public BigDecimal getTotalBruto() {
		return totalBruto;
	}

	public void setTotalBruto(BigDecimal totalBruto) {
		this.totalBruto = totalBruto;
	}

	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
	}

	public BigDecimal getTotalLiquido() {
		return totalLiquido;
	}

	public void setTotalLiquido(BigDecimal totalLiquido) {
		this.totalLiquido = totalLiquido;
	}

}
