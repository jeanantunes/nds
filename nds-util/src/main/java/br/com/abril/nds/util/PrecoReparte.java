package br.com.abril.nds.util;

import java.math.BigDecimal;

public class PrecoReparte {
	
	private BigDecimal precoDesconto;
	
	private BigDecimal quantidade;

	public BigDecimal getPrecoDesconto() {
		return precoDesconto;
	}

	public void setPrecoDesconto(BigDecimal precoDesconto) {
		this.precoDesconto = precoDesconto;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}
}