package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RelatorioDetalheGarantiaDTO implements Serializable{

	private static final long serialVersionUID = 4858065785363185098L;
	
	private Integer cota;
	private String nome;
	private String garantia;
	private Date vencto;
	private BigDecimal vlrGarantia;
	private BigDecimal faturamento;
	private BigDecimal garantiaFaturamento;
	public Integer getCota() {
		return cota;
	}
	public void setCota(Integer cota) {
		this.cota = cota;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getGarantia() {
		return garantia;
	}
	public void setGarantia(String garantia) {
		this.garantia = garantia;
	}
	public Date getVencto() {
		return vencto;
	}
	public void setVencto(Date vencto) {
		this.vencto = vencto;
	}
	public BigDecimal getVlrGarantia() {
		return vlrGarantia;
	}
	public void setVlrGarantia(BigDecimal vlrGarantia) {
		this.vlrGarantia = vlrGarantia;
	}
	public BigDecimal getFaturamento() {
		return faturamento;
	}
	public void setFaturamento(BigDecimal faturamento) {
		this.faturamento = faturamento;
	}
	public BigDecimal getGarantiaFaturamento() {
		return garantiaFaturamento;
	}
	public void setGarantiaFaturamento(BigDecimal garantiaFaturamento) {
		this.garantiaFaturamento = garantiaFaturamento;
	}
	
	
	

}
