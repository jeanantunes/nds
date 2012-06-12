package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProdutoEdicaoSlipDTO implements Serializable {

	private String nomeProduto;
	
	private Long idProdutoEdicao;
	
	private Long numeroEdicao;
	
	private BigDecimal reparte;
	
	private BigDecimal encalhe;
	
	private BigDecimal quantidadeEfetiva;
	
	private BigDecimal precoVenda;
	
	private BigDecimal valorTotal;

	private Date dataRecolhimentoDistribuidor;
	
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
	 * Obtém quantidadeEfetiva
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getQuantidadeEfetiva() {
		return quantidadeEfetiva;
	}
	
	/**
	 * Atribuí quantidadeEfetiva
	 * @param quantidadeEfetiva 
	 */
	public void setQuantidadeEfetiva(BigDecimal quantidadeEfetiva) {
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
	
	
}
