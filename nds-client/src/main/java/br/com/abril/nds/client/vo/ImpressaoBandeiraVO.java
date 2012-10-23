package br.com.abril.nds.client.vo;

import java.io.Serializable;

public class ImpressaoBandeiraVO implements Serializable  {

	public String getTipoOperacao() {
		return tipoOperacao;
	}
	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -8669011482729203456L;
	
	
	private String tipoOperacao;
	private Integer semana;
	private String codigoPracaProcon;
	private String praca;
	private String destino;
	private String canal;
	
	
	
	public Integer getSemana() {
		return semana;
	}
	public void setSemana(Integer semana) {
		this.semana = semana;
	}
	public String getCodigoPracaProcon() {
		return codigoPracaProcon;
	}
	public void setCodigoPracaProcon(String codigoPracaProcon) {
		this.codigoPracaProcon = codigoPracaProcon;
	}
	public String getPraca() {
		return praca;
	}
	public void setPraca(String praca) {
		this.praca = praca;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getCanal() {
		return canal;
	}
	public void setCanal(String canal) {
		this.canal = canal;
	}
	
}
