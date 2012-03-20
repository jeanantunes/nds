package br.com.abril.nds.dto;

import java.math.BigDecimal;

public class ProdutoValorDTO {

	private BigDecimal preco;
	private BigDecimal quantidade;
	
	public BigDecimal getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}
	public BigDecimal getPreco() {
		return preco;
	}
	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}
	public Double getTotal() {
		return (preco.doubleValue() * quantidade.doubleValue());
	}
}

