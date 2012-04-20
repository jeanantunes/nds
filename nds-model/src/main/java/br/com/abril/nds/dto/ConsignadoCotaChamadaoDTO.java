package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ConsignadoCotaChamadaoDTO implements Serializable{
	
	private static final long serialVersionUID = 5175618607088311661L;

	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long numeroEdicao;
	
	private BigDecimal precoCapa;
	
	private BigDecimal precoComDesconto;
	
	private BigDecimal reparte;
	
	private String nomeFornecedor;

	private Date dataRecolhimento;
	
	private BigDecimal valorTotal;

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
	 * @return the precoCapa
	 */
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	/**
	 * @param precoCapa the precoCapa to set
	 */
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}

	/**
	 * @return the precoComDesconto
	 */
	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}

	/**
	 * @param precoComDesconto the precoComDesconto to set
	 */
	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}

	/**
	 * @return the reparte
	 */
	public BigDecimal getReparte() {
		return reparte;
	}

	/**
	 * @param reparte the reparte to set
	 */
	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
	}

	/**
	 * @return the nomeFornecedor
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	/**
	 * @param nomeFornecedor the nomeFornecedor to set
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
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

}
