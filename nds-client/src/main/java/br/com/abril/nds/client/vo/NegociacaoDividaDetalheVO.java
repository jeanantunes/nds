package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class NegociacaoDividaDetalheVO implements Serializable{

	private static final long serialVersionUID = 6521983915168304492L;
	
	private Date data;
	
	private String tipo = "";
	
	private String valor;
	
	private Double valorDouble;
		
	private String observacao;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
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

	public void setValor(BigDecimal valor) {
		this.valorDouble = valor.doubleValue();
		this.valor = valor.toString();
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Double getValorDouble() {
		return valorDouble;
	}

	public void setValorDouble(Double valorDouble) {
		this.valorDouble = valorDouble;
	}
	
	

} 
