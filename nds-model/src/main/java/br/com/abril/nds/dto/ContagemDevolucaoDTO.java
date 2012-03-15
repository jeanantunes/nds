package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ContagemDevolucaoDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private Long idMovimentoEstoqueCota;
	private Long idConferenciaEncParcial;
	private String codigoProduto;
	private String nomeProduto;
	private Long numeroEdicao;
	private BigDecimal precoVenda;
	private BigDecimal qtdDevolucao;
	private BigDecimal qtdNota;
	private BigDecimal valorTotal;
	
	
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}
	public BigDecimal getQtdDevolucao() {
		return qtdDevolucao;
	}
	public void setQtdDevolucao(BigDecimal qtdDevolucao) {
		this.qtdDevolucao = qtdDevolucao;
	}
	public BigDecimal getQtdNota() {
		return qtdNota;
	}
	public void setQtdNota(BigDecimal qtdNota) {
		this.qtdNota = qtdNota;
	}
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	public Long getIdMovimentoEstoqueCota() {
		return idMovimentoEstoqueCota;
	}
	public void setIdMovimentoEstoqueCota(Long idMovimentoEstoqueCota) {
		this.idMovimentoEstoqueCota = idMovimentoEstoqueCota;
	}
	public Long getIdConferenciaEncParcial() {
		return idConferenciaEncParcial;
	}
	public void setIdConferenciaEncParcial(Long idConferenciaEncParcial) {
		this.idConferenciaEncParcial = idConferenciaEncParcial;
	}
	
	
}
