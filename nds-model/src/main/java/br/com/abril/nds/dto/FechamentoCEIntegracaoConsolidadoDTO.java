package br.com.abril.nds.dto;

import java.math.BigDecimal;

public class FechamentoCEIntegracaoConsolidadoDTO {

	private BigDecimal totalBruto;

	private BigDecimal totalDesconto;

	private BigDecimal totalLiquido;
	
	private BigDecimal totalVendaApurada;
	
	private BigDecimal totalVendaInformada;
	
	private BigDecimal totalCreditoApurado;
	
	private BigDecimal totalCreditoInformado;
	
	private BigDecimal totalMargemInformado;
	
	private BigDecimal toatalMargemApurado;
	
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

	public BigDecimal getTotalVendaApurada() {
		return totalVendaApurada;
	}

	public void setTotalVendaApurada(BigDecimal totalVendaApurada) {
		this.totalVendaApurada = totalVendaApurada;
	}

	public BigDecimal getTotalVendaInformada() {
		return totalVendaInformada;
	}

	public void setTotalVendaInformada(BigDecimal totalVendaInformada) {
		this.totalVendaInformada = totalVendaInformada;
	}

	public BigDecimal getTotalCreditoApurado() {
		return totalCreditoApurado;
	}

	public void setTotalCreditoApurado(BigDecimal totalCreditoApurado) {
		this.totalCreditoApurado = totalCreditoApurado;
	}

	public BigDecimal getTotalCreditoInformado() {
		return totalCreditoInformado;
	}

	public void setTotalCreditoInformado(BigDecimal totalCreditoInformado) {
		this.totalCreditoInformado = totalCreditoInformado;
	}

	public BigDecimal getTotalMargemInformado() {
		return totalMargemInformado;
	}

	public void setTotalMargemInformado(BigDecimal totalMargemInformado) {
		this.totalMargemInformado = totalMargemInformado;
	}

	public BigDecimal getToatalMargemApurado() {
		return toatalMargemApurado;
	}

	public void setToatalMargemApurado(BigDecimal toatalMargemApurado) {
		this.toatalMargemApurado = toatalMargemApurado;
	}
}
