package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.aprovacao.StatusAprovacao;

public class ContagemDevolucaoDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String codigoProduto;
	private String nomeProduto;
	private Long numeroEdicao;
	private BigDecimal precoVenda;
	private BigDecimal qtdDevolucao;
	private BigDecimal valorTotal;
	private BigDecimal qtdNota;
	private BigDecimal diferenca;
	private Date dataRecolhimentoDistribuidor;
	private Date dataConfEncalheParcial;
	private Date dataAprovacao;
	private StatusAprovacao statusAprovacao;
	
	public ContagemDevolucaoDTO(){}
	
	public ContagemDevolucaoDTO (
		
			String codigoProduto, 
			String nomeProduto, 
			Long numeroEdicao,
			BigDecimal precoVenda, 
			BigDecimal qtdDevolucao,
			BigDecimal qtdNota,
			Date dataRecolhimentoDistribuidor) {
		
		super();
		
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.precoVenda = precoVenda;
		this.qtdDevolucao = qtdDevolucao;
		this.qtdNota = qtdNota;
		this.dataRecolhimentoDistribuidor = dataRecolhimentoDistribuidor;
	}

	public ContagemDevolucaoDTO (
			
			String codigoProduto, 
			String nomeProduto, 
			Long numeroEdicao,
			BigDecimal precoVenda, 
			Date dataRecolhimentoDistribuidor) {
		
		super();
		
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.precoVenda = precoVenda;
		this.dataRecolhimentoDistribuidor = dataRecolhimentoDistribuidor;
	}
	

	public BigDecimal getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(BigDecimal diferenca) {
		this.diferenca = diferenca;
	}

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
	public Date getDataRecolhimentoDistribuidor() {
		return dataRecolhimentoDistribuidor;
	}
	public void setDataRecolhimentoDistribuidor(Date dataRecolhimentoDistribuidor) {
		this.dataRecolhimentoDistribuidor = dataRecolhimentoDistribuidor;
	}
	public Date getDataConfEncalheParcial() {
		return dataConfEncalheParcial;
	}
	public void setDataConfEncalheParcial(Date dataConfEncalheParcial) {
		this.dataConfEncalheParcial = dataConfEncalheParcial;
	}
	public Date getDataAprovacao() {
		return dataAprovacao;
	}
	public void setDataAprovacao(Date dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}
	public StatusAprovacao getStatusAprovacao() {
		return statusAprovacao;
	}
	public void setStatusAprovacao(StatusAprovacao statusAprovacao) {
		this.statusAprovacao = statusAprovacao;
	}
	
}
