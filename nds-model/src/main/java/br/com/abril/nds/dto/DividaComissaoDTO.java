package br.com.abril.nds.dto;


public class DividaComissaoDTO {
	
	private String porcentagem;
	private String valorDivida;
	private String valorPago;
	private String valorResidual;
	
	public String getPorcentagem() {
		return porcentagem;
	}
	public void setPorcentagem(String porcentagem) {
		this.porcentagem = porcentagem;
	}
	public String getValorDivida() {
		return valorDivida;
	}
	public void setValorDivida(String valorDivida) {
		this.valorDivida = valorDivida;
	}
	public String getValorPago() {
		return valorPago;
	}
	public void setValorPago(String valorPago) {
		this.valorPago = valorPago;
	}
	public String getValorResidual() {
		return valorResidual;
	}
	public void setValorResidual(String valorResidual) {
		this.valorResidual = valorResidual;
	}
}
