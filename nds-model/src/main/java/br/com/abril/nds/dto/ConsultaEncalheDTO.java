package br.com.abril.nds.dto;

import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;

public class ConsultaEncalheDTO {

	private Long idProdutoEdicao;
	
	@Export(label="Código")
	private String codigoProduto;
	
	@Export(label="Produto")
	private String nomeProduto;
	
	@Export(label="Edição")
	private Long numeroEdicao;
	
	@Export(label="Preço Capa R$")
	private BigDecimal precoVenda;
	
	@Export(label="Preço com Desc. R$")
	private BigDecimal precoComDesconto;
	
	@Export(label="Reparte")
	private BigDecimal reparte;
	
	@Export(label="Encalhe")
	private BigDecimal encalhe;
	
	@Export(label="Fornecedor")
	private String fornecedor;
	
	@Export(label="Total R$")
	private BigDecimal total;
	
	@Export(label="Recolhimento")
	private Integer recolhimento;
	

	/**
	 * Obtém idProdutoEdicao
	 *
	 * @return Long
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * Atribuí idProdutoEdicao
	 * @param idProdutoEdicao 
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	/**
	 * Obtém codigoProduto
	 *
	 * @return String
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * Atribuí codigoProduto
	 * @param codigoProduto 
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	/**
	 * Obtém nomeProduto
	 *
	 * @return String
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * Atribuí nomeProduto
	 * @param nomeProduto 
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	/**
	 * Obtém numeroEdicao
	 *
	 * @return Long
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * Atribuí numeroEdicao
	 * @param numeroEdicao 
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * Obtém precoVenda
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * Atribuí precoVenda
	 * @param precoVenda 
	 */
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	/**
	 * Obtém precoComDesconto
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}

	/**
	 * Atribuí precoComDesconto
	 * @param precoComDesconto 
	 */
	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}

	/**
	 * Obtém reparte
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getReparte() {
		return reparte;
	}

	/**
	 * Atribuí reparte
	 * @param reparte 
	 */
	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
	}

	/**
	 * Obtém encalhe
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getEncalhe() {
		return encalhe;
	}

	/**
	 * Atribuí encalhe
	 * @param encalhe 
	 */
	public void setEncalhe(BigDecimal encalhe) {
		this.encalhe = encalhe;
	}

	/**
	 * Obtém fornecedor
	 *
	 * @return String
	 */
	public String getFornecedor() {
		return fornecedor;
	}

	/**
	 * Atribuí fornecedor
	 * @param fornecedor 
	 */
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	/**
	 * Obtém total
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * Atribuí total
	 * @param total 
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	/**
	 * Obtém recolhimento
	 *
	 * @return Integer
	 */
	public Integer getRecolhimento() {
		return recolhimento;
	}

	/**
	 * Atribuí recolhimento
	 * @param recolhimento 
	 */
	public void setRecolhimento(Integer recolhimento) {
		this.recolhimento = recolhimento;
	}
	
}
