package br.com.abril.nds.client.vo;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaEncalheVO {

	private String idProdutoEdicao;
	
	private String dataRecolhimento;

	private String dataMovimento;

	private String idFornecedor;

	private String idCota;

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
	
	@Export(label="Valor R$")
	private String valor;
	
	@Export(label="Valor c/ Desc.")
	private String valorComDesconto;
	
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

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the valorComDesconto
	 */
	public String getValorComDesconto() {
		return valorComDesconto;
	}

	/**
	 * @param valorComDesconto the valorComDesconto to set
	 */
	public void setValorComDesconto(String valorComDesconto) {
		this.valorComDesconto = valorComDesconto;
	}

	/**
	 * @return the dataRecolhimento
	 */
	public String getDataRecolhimento() {
		return dataRecolhimento;
	}

	/**
	 * @param dataRecolhimento the dataRecolhimento to set
	 */
	public void setDataRecolhimento(String dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	/**
	 * @return the dataMovimento
	 */
	public String getDataMovimento() {
		return dataMovimento;
	}

	/**
	 * @param dataMovimento the dataMovimento to set
	 */
	public void setDataMovimento(String dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	/**
	 * @return the idFornecedor
	 */
	public String getIdFornecedor() {
		return idFornecedor;
	}

	/**
	 * @param idFornecedor the idFornecedor to set
	 */
	public void setIdFornecedor(String idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	/**
	 * @return the idCota
	 */
	public String getIdCota() {
		return idCota;
	}

	/**
	 * @param idCota the idCota to set
	 */
	public void setIdCota(String idCota) {
		this.idCota = idCota;
	}

	
}
