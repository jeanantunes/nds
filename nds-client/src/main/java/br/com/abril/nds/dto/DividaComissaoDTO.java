package br.com.abril.nds.dto;

import java.math.BigDecimal;

public class DividaComissaoDTO {
	
	private BigDecimal porcentagem;
	private BigDecimal valorDivida;
	private BigDecimal valorPago;
	private BigDecimal valorResidual;
	
	public BigDecimal getPorcentagem() {
		return porcentagem;
	}
	public void setPorcentagem(BigDecimal porcentagem) {
		this.porcentagem = porcentagem;
	}
	public BigDecimal getValorDivida() {
		return valorDivida;
	}
	public void setValorDivida(BigDecimal valorDivida) {
		this.valorDivida = valorDivida;
	}
	public BigDecimal getValorPago() {
		return valorPago;
	}
	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}
	public BigDecimal getValorResidual() {
		return valorResidual;
	}
	public void setValorResidual(BigDecimal valorResidual) {
		this.valorResidual = valorResidual;
	}
	
	


}
