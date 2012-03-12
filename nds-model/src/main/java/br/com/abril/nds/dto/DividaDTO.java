package br.com.abril.nds.dto;

public class DividaDTO {
	
	private String vencimento;
	private Double valor;
	
	public DividaDTO() {
	}
	
	public DividaDTO(String vencimento, Double valor) {
		this.valor = valor;
		this.vencimento = vencimento;
	}
	
	public String getVencimento() {
		return vencimento;
	}
	public void setVencimento(String vencimento) {
		this.vencimento = vencimento;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
}