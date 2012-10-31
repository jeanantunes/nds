package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ProdutoValorDTO {

	private BigDecimal preco;
	private BigInteger quantidade;
	
	public BigInteger getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(BigInteger quantidade) {
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

