package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class VisaoEstoqueDTO implements Serializable{

	private static final long serialVersionUID = -4171186893803302134L;
	
	private String estoque;
	
	private Long produtos;
	
	private Long exemplares;
	
	private BigDecimal valor;

	public String getEstoque() {
		return estoque;
	}

	public void setEstoque(String estoque) {
		this.estoque = estoque;
	}

	public Long getProdutos() {
		return produtos;
	}

	public void setProdutos(Long produtos) {
		this.produtos = produtos;
	}

	public Long getExemplares() {
		return exemplares;
	}

	public void setExemplares(Long exemplares) {
		this.exemplares = exemplares;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	
}
