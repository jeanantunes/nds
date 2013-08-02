package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.model.estoque.TipoDiferenca;

public class DetalheItemNotaFiscalDTO implements Serializable {

	private static final long serialVersionUID = -3303109650463467609L;

	private Long codigoItem;
	
	private String codigoProduto;

	private String nomeProduto;
	
	private Long numeroEdicao;
	
	private BigInteger quantidadeExemplares;
	
	private BigInteger sobrasFaltas;
	
	private BigDecimal precoVenda;
	
	private BigDecimal precoComDesconto;
	
	private BigDecimal precoTotalComDesconto;

	private TipoDiferenca tipoDiferenca;
	
	private BigDecimal valorTotal;
	
	private Long idProdutoEdicao;
	
	protected Integer pacotePadrao;
	
	private BigDecimal desconto;
	
	public DetalheItemNotaFiscalDTO() { }

	/**
	 * @return the codigoItem
	 */
	public Long getCodigoItem() {
		return codigoItem;
	}

	/**
	 * @param codigoItem the codigoItem to set
	 */
	public void setCodigoItem(Long codigoItem) {
		this.codigoItem = codigoItem;
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
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * @return the quantidadeExemplares
	 */
	public BigInteger getQuantidadeExemplares() {
		return quantidadeExemplares;
	}

	/**
	 * @param quantidadeExemplares the quantidadeExemplares to set
	 */
	public void setQuantidadeExemplares(BigInteger quantidadeExemplares) {
		this.quantidadeExemplares = quantidadeExemplares;
	}

	/**
	 * @return the sobrasFaltas
	 */
	public BigInteger getSobrasFaltas() {
		return sobrasFaltas;
	}

	/**
	 * @param sobrasFaltas the sobrasFaltas to set
	 */
	public void setSobrasFaltas(BigInteger sobrasFaltas) {
		this.sobrasFaltas = sobrasFaltas;
	}
	
	/**
	 * @return the precoComDesconto
	 */
	public BigDecimal getPrecoComDesconto() {
		
		this.precoComDesconto = (this.getPrecoTotalComDesconto()
				                    .divide(this.quantidadeExemplares == null ? BigDecimal.ONE
										: new BigDecimal(
												this.quantidadeExemplares),3));
		
		return precoComDesconto;
	}

	/**
	 * @param precoComDesconto the precoComDesconto to set
	 */
	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		
		this.precoComDesconto = precoComDesconto;
	}

	/**
	 * @return the precoTotalComDesconto
	 */
	public BigDecimal getPrecoTotalComDesconto() {
		
		this.precoTotalComDesconto = this.valorTotal
				.subtract(this.desconto == null ? BigDecimal.ZERO
						: this.desconto);
		
		return precoTotalComDesconto;
	}

	/**
	 * @param precoTotalComDesconto the precoTotalComDesconto to set
	 */
	public void setPrecoTotalComDesconto(BigDecimal precoTotalComDesconto) {
		this.precoTotalComDesconto = precoTotalComDesconto;
	}

	/**
	 * @return the tipoDiferenca
	 */
	public TipoDiferenca getTipoDiferenca() {
		return tipoDiferenca;
	}

	/**
	 * @param tipoDiferenca the tipoDiferenca to set
	 */
	public void setTipoDiferenca(TipoDiferenca tipoDiferenca) {
		this.tipoDiferenca = tipoDiferenca;
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
	 * @return the precoVenda
	 */
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	/**
	 * @return the valorTotal
	 */
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * @return the idProdutoEdicao
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	/**
	 * @return the pacotePadrao
	 */
	public Integer getPacotePadrao() {
		return pacotePadrao;
	}

	/**
	 * @param pacotePadrao the pacotePadrao to set
	 */
	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	
}
