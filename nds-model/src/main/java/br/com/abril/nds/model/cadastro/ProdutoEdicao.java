package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.com.abril.nds.model.planejamento.Lancamento;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "PRODUTO_EDICAO", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"NUMERO_EDICAO", "PRODUTO_ID" })})
@SequenceGenerator(name="PROD_ED_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "PRODUTO")
public class ProdutoEdicao implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "PROD_ED_SEQ")
	@Column(name = "ID")
	protected Long id;
	
	@Column(name  = "NUMERO_EDICAO", nullable = false)
	protected Long numeroEdicao;
	
	@Column(name = "PRECO_VENDA", nullable = false)
	protected BigDecimal precoVenda;
	
	@Column(name = "DESCONTO")
	protected BigDecimal desconto = BigDecimal.ZERO;
	
	@Column(name = "PACOTE_PADRAO", nullable = false)
	protected int pacotePadrao;
	
	@Column(name = "PEB", nullable = false)
	protected int peb;
	
	@Column(name = "PRECO_CUSTO")
	protected BigDecimal precoCusto;
	
	@Column(name = "PESO", nullable = false)
	protected BigDecimal peso;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_ID")
	protected Produto produto;
	
	@OneToMany(mappedBy = "produtoEdicao")
	protected Set<Lancamento> lancamentos = new HashSet<Lancamento>();
	
	@Column(name = "POSSUI_BRINDE", nullable = false)
	protected boolean possuiBrinde;
	
	/**
	 * Percentual de expectativa de venda do produto
	 */
	@Column(name = "EXPECTATIVA_VENDA")
	protected BigDecimal expectativaVenda;
	
	/**
	 * Flag indicando se o produto permite vale desconto
	 */
	@Column(name = "PERMITE_VALE_DESCONTO")
	protected boolean permiteValeDesconto;
	
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
	

	public Set<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(Set<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}
	
	public boolean isPossuiBrinde() {
		return possuiBrinde;
	}
	
	public void setPossuiBrinde(boolean possuiBrinde) {
		this.possuiBrinde = possuiBrinde;
	}
	
	public BigDecimal getExpectativaVenda() {
		return expectativaVenda;
	}
	
	public void setExpectativaVenda(BigDecimal expectativaVenda) {
		this.expectativaVenda = expectativaVenda;
	}
	
	public boolean isPermiteValeDesconto() {
		return permiteValeDesconto;
	}
	
	public void setPermiteValeDesconto(boolean permiteValeDesconto) {
		this.permiteValeDesconto = permiteValeDesconto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoEdicao other = (ProdutoEdicao) obj;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new StringBuilder(produto.toString()).append("-")
				.append(numeroEdicao).toString();
	}

}
