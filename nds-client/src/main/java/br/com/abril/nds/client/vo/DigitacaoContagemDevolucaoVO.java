package br.com.abril.nds.client.vo;

import java.io.Serializable;

public class DigitacaoContagemDevolucaoVO implements Serializable {

	/**
	 * SERIAL VERSION UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String idConferenciaEncParcial;
	private String codigoProduto;
	private String nomeProduto;
	private String numeroEdicao;
	private String precoVenda;
	private String desconto;
	private String qtdDevolucao;
	private String qtdNota;
	private String valorTotal;
	private String valorTotalComDesconto;
	private String diferenca;
	private String dataRecolhimentoDistribuidor;
	private boolean isEdicaoFechada;
	
	/**
	 * @return the idConferenciaEncParcial
	 */
	public String getIdConferenciaEncParcial() {
		return idConferenciaEncParcial;
	}
	/**
	 * @param idConferenciaEncParcial the idConferenciaEncParcial to set
	 */
	public void setIdConferenciaEncParcial(String idConferenciaEncParcial) {
		this.idConferenciaEncParcial = idConferenciaEncParcial;
	}
	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}
	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	/**
	 * @return the nomeProduto
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}
	/**
	 * @param nomeProduto the nomeProduto to set
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	/**
	 * @return the numeroEdicao
	 */
	public String getNumeroEdicao() {
		return numeroEdicao;
	}
	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	/**
	 * @return the precoVenda
	 */
	public String getPrecoVenda() {
		return precoVenda;
	}
	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(String precoVenda) {
		this.precoVenda = precoVenda;
	}
	/**
	 * @return the qtdDevolucao
	 */
	public String getQtdDevolucao() {
		return qtdDevolucao;
	}
	/**
	 * @param qtdDevolucao the qtdDevolucao to set
	 */
	public void setQtdDevolucao(String qtdDevolucao) {
		this.qtdDevolucao = qtdDevolucao;
	}
	/**
	 * @return the qtdNota
	 */
	public String getQtdNota() {
		return qtdNota;
	}
	/**
	 * @param qtdNota the qtdNota to set
	 */
	public void setQtdNota(String qtdNota) {
		this.qtdNota = qtdNota;
	}
	/**
	 * @return the valorTotal
	 */
	public String getValorTotal() {
		return valorTotal;
	}
	/**
	 * @return the diferenca
	 */
	public String getDiferenca() {
		return diferenca;
	}
	/**
	 * @param diferenca the diferenca to set
	 */
	public void setDiferenca(String diferenca) {
		this.diferenca = diferenca;
	}
	/**
	 * @return the dataRecolhimentoDistribuidor
	 */
	public String getDataRecolhimentoDistribuidor() {
		return dataRecolhimentoDistribuidor;
	}
	/**
	 * @param dataRecolhimentoDistribuidor the dataRecolhimentoDistribuidor to set
	 */
	public void setDataRecolhimentoDistribuidor(String dataRecolhimentoDistribuidor) {
		this.dataRecolhimentoDistribuidor = dataRecolhimentoDistribuidor;
	}
	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}
	/**
	 * @return the desconto
	 */
	public String getDesconto() {
		return desconto;
	}
	/**
	 * @param desconto the desconto to set
	 */
	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}
	/**
	 * @return the valorTotalComDesconto
	 */
	public String getValorTotalComDesconto() {
		return valorTotalComDesconto;
	}
	/**
	 * @param valorTotalComDesconto the valorTotalComDesconto to set
	 */
	public void setValorTotalComDesconto(String valorTotalComDesconto) {
		this.valorTotalComDesconto = valorTotalComDesconto;
	}
	/**
	 * @return the isEdicaoFechada
	 */
	public boolean isEdicaoFechada() {
		return isEdicaoFechada;
	}
	/**
	 * @param isEdicaoFechada the isEdicaoFechada to set
	 */
	public void setEdicaoFechada(boolean isEdicaoFechada) {
		this.isEdicaoFechada = isEdicaoFechada;
	}
	
}
