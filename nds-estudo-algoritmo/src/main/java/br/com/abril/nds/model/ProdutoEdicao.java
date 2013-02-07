package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.List;

public class ProdutoEdicao {

	private Long id;
	private Long numeroEdicao;
	private Long peso;
	private BigDecimal reparteDistribuido;
	private List<Produto> produtos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public Long getPeso() {
		return peso;
	}

	public void setPeso(Long peso) {
		this.peso = peso;
	}

	public BigDecimal getReparteDistribuido() {
		return reparteDistribuido;
	}

	public void setReparteDistribuido(BigDecimal reparteDistribuido) {
		this.reparteDistribuido = reparteDistribuido;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

}
