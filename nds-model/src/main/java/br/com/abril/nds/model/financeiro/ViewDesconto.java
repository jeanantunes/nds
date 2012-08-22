package br.com.abril.nds.model.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VIEW_DESCONTO")
public class ViewDesconto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6426590627621374640L;
	
	@Id
	private Long id;

	@Column(name = "DESCONTO")
	private BigDecimal desconto;
	
	@Column(name = "COTA_ID")
	private Long cotaId;
	
	@Column(name = "PRODUTO_ID")
	private Long produtoId;
	
	@Column(name = "FORNECEDOR_ID")
	private Long fornecedorId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public Long getCotaId() {
		return cotaId;
	}

	public void setCotaId(Long cotaId) {
		this.cotaId = cotaId;
	}

	public Long getProdutoId() {
		return produtoId;
	}

	public void setProdutoId(Long produtoId) {
		this.produtoId = produtoId;
	}

	public Long getFornecedorId() {
		return fornecedorId;
	}

	public void setFornecedorId(Long fornecedorId) {
		this.fornecedorId = fornecedorId;
	}
}