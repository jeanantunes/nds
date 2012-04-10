package br.com.abril.nds.client.vo;

import br.com.abril.nds.util.export.Export;

public class ConsultaEncalheVO {

	private String idProdutoEdicao;
	
	@Export(label="Código")
	private String codigoProduto;
	
	@Export(label="Produto")
	private String nomeProduto;
	
	@Export(label="Edição")
	private String numeroEdicao;
	
	@Export(label="Preço Capa R$")
	private String precoVenda;
	
	@Export(label="Preço com Desc. R$")
	private String precoComDesconto;
	
	@Export(label="Reparte")
	private String reparte;
	
	@Export(label="Encalhe")
	private String encalhe;
	
	@Export(label="Fornecedor")
	private String fornecedor;
	
	@Export(label="Total R$")
	private String total;
	
	@Export(label="Recolhimento")
	private String recolhimento;

	/**
	 * Obtém idProdutoEdicao
	 *
	 * @return String
	 */
	public String getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * Atribuí idProdutoEdicao
	 * @param idProdutoEdicao 
	 */
	public void setIdProdutoEdicao(String idProdutoEdicao) {
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
	 * @return String
	 */
	public String getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * Atribuí numeroEdicao
	 * @param numeroEdicao 
	 */
	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * Obtém precoVenda
	 *
	 * @return String
	 */
	public String getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * Atribuí precoVenda
	 * @param precoVenda 
	 */
	public void setPrecoVenda(String precoVenda) {
		this.precoVenda = precoVenda;
	}

	/**
	 * Obtém precoComDesconto
	 *
	 * @return String
	 */
	public String getPrecoComDesconto() {
		return precoComDesconto;
	}

	/**
	 * Atribuí precoComDesconto
	 * @param precoComDesconto 
	 */
	public void setPrecoComDesconto(String precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}

	/**
	 * Obtém reparte
	 *
	 * @return String
	 */
	public String getReparte() {
		return reparte;
	}

	/**
	 * Atribuí reparte
	 * @param reparte 
	 */
	public void setReparte(String reparte) {
		this.reparte = reparte;
	}

	/**
	 * Obtém encalhe
	 *
	 * @return String
	 */
	public String getEncalhe() {
		return encalhe;
	}

	/**
	 * Atribuí encalhe
	 * @param encalhe 
	 */
	public void setEncalhe(String encalhe) {
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
	 * @return String
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * Atribuí total
	 * @param total 
	 */
	public void setTotal(String total) {
		this.total = total;
	}

	/**
	 * Obtém recolhimento
	 *
	 * @return String
	 */
	public String getRecolhimento() {
		return recolhimento;
	}

	/**
	 * Atribuí recolhimento
	 * @param recolhimento 
	 */
	public void setRecolhimento(String recolhimento) {
		this.recolhimento = recolhimento;
	}

	
}
