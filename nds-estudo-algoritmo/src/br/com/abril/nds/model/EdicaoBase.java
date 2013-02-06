package br.com.abril.nds.model;

import java.math.BigDecimal;

public class EdicaoBase extends Produto {

	private BigDecimal reparte;
	private BigDecimal venda;
	private Boolean parcial;
	private Integer pacotePadrao;
	
	public BigDecimal getReparte() {
		return reparte;
	}
	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
	}
	public BigDecimal getVenda() {
		return venda;
	}
	public void setVenda(BigDecimal venda) {
		this.venda = venda;
	}
	public Boolean getParcial() {
		return parcial;
	}
	public void setParcial(Boolean parcial) {
		this.parcial = parcial;
	}
	public Integer getPacotePadrao() {
		return pacotePadrao;
	}
	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
}
