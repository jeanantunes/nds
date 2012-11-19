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
	private Long id = 1L;

	@Column(name = "DESCONTO")
	private BigDecimal desconto;
	
	@Column(name = "COTA_ID")
	private Long cotaId;
	
	@Column(name = "PRODUTO_EDICAO_ID")
	private Long produtoEdicaoId;
	
	@Column(name = "NUMERO_EDICAO")
	private Long numeroEdicao;
	
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

	public Long getProdutoEdicaoId() {
		return produtoEdicaoId;
	}

	public void setProdutoEdicaoId(Long produtoEdicaoId) {
		this.produtoEdicaoId = produtoEdicaoId;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public Long getFornecedorId() {
		return fornecedorId;
	}

	public void setFornecedorId(Long fornecedorId) {
		this.fornecedorId = fornecedorId;
	}
}