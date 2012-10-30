package br.com.abril.nds.client.vo;

import java.io.Serializable;

public class NegociacaoDividaDetalheVO implements Serializable{

	private static final long serialVersionUID = 6521983915168304492L;
	
	private String data;
	
	private String tipo = "";
	
	private String valor;
	
	private String observacao;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	

} 
