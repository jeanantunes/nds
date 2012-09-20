package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ProdutoEdicaoSlipDTO implements Serializable {

	private Long idChamadaEncalhe;
	
	private String nomeProduto;
	
	private Long idProdutoEdicao;
	
	private Long numeroEdicao;
	
	private BigInteger reparte;
	
	private BigInteger encalhe;
	
	private BigInteger quantidadeEfetiva;
	
	private BigDecimal precoVenda;
	
	private BigDecimal valorTotal;

	private Date dataRecolhimentoDistribuidor;
	
	private Date dataOperacao;
	
	private Date dataRecolhimento;
	
	private String ordinalDiaConferenciaEncalhe;
	
	private String qtdeTotalProdutos;
	
	private String valorTotalEncalhe;
	
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
	 * Obtém reparte
	 *
	 * @return BigInteger
	 */
	public BigInteger getReparte() {
		return reparte;
	}

	/**
	 * Atribuí reparte
	 * @param reparte 
	 */
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	/**
	 * Obtém encalhe
	 *
	 * @return BigInteger
	 */
	public BigInteger getEncalhe() {
		return encalhe;
	}

	/**
	 * Atribuí encalhe
	 * @param encalhe 
	 */
	public void setEncalhe(BigInteger encalhe) {
		this.encalhe = encalhe;
	}
	
	/**
	 * Obtém quantidadeEfetiva
	 *
	 * @return BigInteger
	 */
	public BigInteger getQuantidadeEfetiva() {
		return quantidadeEfetiva;
	}
	
	/**
	 * Atribuí quantidadeEfetiva
	 * @param quantidadeEfetiva 
	 */
	public void setQuantidadeEfetiva(BigInteger quantidadeEfetiva) {
		this.quantidadeEfetiva = quantidadeEfetiva;
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
	 * Obtém valorTotal
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	/**
	 * Atribuí valorTotal
	 * @param valorTotal 
	 */
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * Obtém dataRecolhimentoDistribuidor
	 *
	 * @return Date
	 */
	public Date getDataRecolhimentoDistribuidor() {
		return dataRecolhimentoDistribuidor;
	}

	/**
	 * Atribuí dataRecolhimentoDistribuidor
	 * @param dataRecolhimentoDistribuidor 
	 */
	public void setDataRecolhimentoDistribuidor(Date dataRecolhimentoDistribuidor) {
		this.dataRecolhimentoDistribuidor = dataRecolhimentoDistribuidor;
	}

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
	 * @return the dataOperacao
	 */
	public Date getDataOperacao() {
		return dataOperacao;
	}

	/**
	 * @param dataOperacao the dataOperacao to set
	 */
	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	/**
	 * @return the dataRecolhimento
	 */
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	/**
	 * @param dataRecolhimento the dataRecolhimento to set
	 */
	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	/**
	 * @return the ordinalDiaConferenciaEncalhe
	 */
	public String getOrdinalDiaConferenciaEncalhe() {
		return ordinalDiaConferenciaEncalhe;
	}

	/**
	 * @param ordinalDiaConferenciaEncalhe the ordinalDiaConferenciaEncalhe to set
	 */
	public void setOrdinalDiaConferenciaEncalhe(String ordinalDiaConferenciaEncalhe) {
		this.ordinalDiaConferenciaEncalhe = ordinalDiaConferenciaEncalhe;
	}

	/**
	 * @return the qtdeTotalProdutos
	 */
	public String getQtdeTotalProdutos() {
		return qtdeTotalProdutos;
	}

	/**
	 * @param qtdeTotalProdutos the qtdeTotalProdutos to set
	 */
	public void setQtdeTotalProdutos(String qtdeTotalProdutos) {
		this.qtdeTotalProdutos = qtdeTotalProdutos;
	}

	/**
	 * @return the valorTotalEncalhe
	 */
	public String getValorTotalEncalhe() {
		return valorTotalEncalhe;
	}

	/**
	 * @param valorTotalEncalhe the valorTotalEncalhe to set
	 */
	public void setValorTotalEncalhe(String valorTotalEncalhe) {
		this.valorTotalEncalhe = valorTotalEncalhe;
	}

	/**
	 * @return the idChamadaEncalhe
	 */
	public Long getIdChamadaEncalhe() {
		return idChamadaEncalhe;
	}

	/**
	 * @param idChamadaEncalhe the idChamadaEncalhe to set
	 */
	public void setIdChamadaEncalhe(Long idChamadaEncalhe) {
		this.idChamadaEncalhe = idChamadaEncalhe;
	}

}
