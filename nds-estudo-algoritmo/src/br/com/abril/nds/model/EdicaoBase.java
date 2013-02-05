package br.com.abril.nds.model;

import java.math.BigDecimal;

public class EdicaoBase extends Produto {

	private BigDecimal reparte;
	private BigDecimal venda;
	
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
}
