package br.com.abril.nds.dto;

import java.io.Serializable;

public class DividaDTO implements Serializable{
	
	private static final long serialVersionUID = -5403191577161993585L;
	
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