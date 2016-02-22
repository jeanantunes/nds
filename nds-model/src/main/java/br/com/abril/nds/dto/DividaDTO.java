package br.com.abril.nds.dto;

import java.io.Serializable;

public class DividaDTO implements Serializable{
	
	private static final long serialVersionUID = -5403191577161993585L;
	
	private String vencimento;
	private String emissao;
	private String valor;
	
	public DividaDTO() {
		
	}
	
	public DividaDTO(String vencimento, String valor) {
		this.valor = valor;
		this.vencimento = vencimento;
	}
	
	public DividaDTO(String vencimento, String emissao, String valor) {
		this.valor = valor;
		this.vencimento = vencimento;
		this.emissao = emissao;
	}
	
	public String getVencimento() {
		return vencimento;
	}
	
	public void setVencimento(String vencimento) {
		this.vencimento = vencimento;
	}
	
	public String getValor() {
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getEmissao() {
		return emissao;
	}

	public void setEmissao(String emissao) {
		this.emissao = emissao;
	}
}