package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ContagemDevolucaoDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private Long idProdutoEdicao;
	
	@Export(label="Código")
	private String codigoProduto;
	
	@Export(label="Produto")
	private String nomeProduto;
	
	@Export(label="Edição")
	private Long numeroEdicao;
	
	@Export(label="Preço Capa R$")
	private BigDecimal precoVenda;
	
	@Export(label="Exemplar Devolução")
	private BigDecimal qtdDevolucao;
	
	@Export(label="Total R$")
	private BigDecimal valorTotal;
	
	@Export(label="Exemplar Nota")
	private BigDecimal qtdNota;
	
	@Export(label="Diferença")
	private BigDecimal diferenca;
	
	private Date dataMovimento;
	
	private Date dataConfEncalheParcial;
	
	private Date dataAprovacao;
	
	private StatusAprovacao statusAprovacao;
	
	public ContagemDevolucaoDTO(){}
	
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
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
	public Date getDataMovimento() {
		return dataMovimento;
	}
	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
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
