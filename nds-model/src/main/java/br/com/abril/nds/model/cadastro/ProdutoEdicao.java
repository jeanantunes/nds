package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "PRODUTO_EDICAO")
public class ProdutoEdicao {

	@Id
	private Long id;
	private BigDecimal precoVenda;
	private BigDecimal desconto;
	private int pacotePadrao;
	private int peb;
	private BigDecimal precoCusto;
	private BigDecimal peso;
	@ManyToOne
	private Produto produto;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}
	
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}
	
	public BigDecimal getDesconto() {
		return desconto;
	}
	
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	
	public int getPacotePadrao() {
		return pacotePadrao;
	}
	
	public void setPacotePadrao(int pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
	
	public int getPeb() {
		return peb;
	}
	
	public void setPeb(int peb) {
		this.peb = peb;
	}
	
	public BigDecimal getPrecoCusto() {
		return precoCusto;
	}
	
	public void setPrecoCusto(BigDecimal precoCusto) {
		this.precoCusto = precoCusto;
	}
	
	public BigDecimal getPeso() {
		return peso;
	}
	
	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}
	
	public Produto getProduto() {
		return produto;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}

}