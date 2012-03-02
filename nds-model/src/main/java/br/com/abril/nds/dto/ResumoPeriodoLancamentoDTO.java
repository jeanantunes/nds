package br.com.abril.nds.dto;

import java.io.Serializable;

public class ResumoPeriodoLancamentoDTO implements Serializable {

	private static final long serialVersionUID = 7487823794102857136L;

	private String data;
	private Long qtdeTitulos;
	private String qtdeExemplares;
	private String pesoTotal;
	private String valorTotal;

	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public Long getQtdeTitulos() {
		return qtdeTitulos;
	}
	
	public void setQtdeTitulos(Long qtdeTitulos) {
		this.qtdeTitulos = qtdeTitulos;
	}
	
	public String getQtdeExemplares() {
		return qtdeExemplares;
	}
	
	public void setQtdeExemplares(String qtdeExemplares) {
		this.qtdeExemplares = qtdeExemplares;
	}
	
	public String getPesoTotal() {
		return pesoTotal;
	}
	
	public void setPesoTotal(String pesoTotal) {
		this.pesoTotal = pesoTotal;
	}
	
	public String getValorTotal() {
		return valorTotal;
	}
	
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
