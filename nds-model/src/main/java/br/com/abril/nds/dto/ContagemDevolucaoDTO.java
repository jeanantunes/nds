package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ContagemDevolucaoDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private Long idProdutoEdicao;
	
	private Long idItemChamadaEncalheFornecedor;
	
	@Export(label="Código", fontSize=9)
	private String codigoProduto;
	
	@Export(label="Produto", fontSize=9, widthPercent=35)
	private String nomeProduto;
	
	@Export(label="Edição", fontSize=9)
	private Long numeroEdicao;
	
	@Export(label="Preço Capa R$", fontSize=9, columnType=ColumnType.MOEDA)
	private BigDecimal precoVenda;
	
	@Export(label="Exemplar Devolução", fontSize=9)
	private BigInteger qtdDevolucao;
	
	@Export(label="Desconto", fontSize=9, columnType=ColumnType.MOEDA)
	private BigDecimal totalComDesconto;
	
	private BigDecimal desconto;
	
	@Export(label="Total R$", fontSize=9, columnType=ColumnType.MOEDA)
	private BigDecimal valorTotal;
	
	@Export(label="Exemplar Nota", fontSize=9)
	private BigInteger qtdNota;
	
	@Export(label="Diferença", fontSize=9)
	private BigInteger diferenca;
	
	private Date dataMovimento;
	
	private Date dataConfEncalheParcial;
	
	private Date dataAprovacao;
	
	private StatusAprovacao statusAprovacao;
	
	private boolean isEdicaoFechada;
	
	public ContagemDevolucaoDTO(){}
	
	
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}
	
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}
	
	public BigInteger getDiferenca() {
		return diferenca;
	}
	
	public void setDiferenca(BigInteger diferenca) {
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
	
	public BigInteger getQtdDevolucao() {
		return qtdDevolucao;
	}
	
	public void setQtdDevolucao(BigInteger qtdDevolucao) {
		this.qtdDevolucao = qtdDevolucao;
	}
	
	public BigInteger getQtdNota() {
		return qtdNota;
	}
	
	public void setQtdNota(BigInteger qtdNota) {
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

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getTotalComDesconto() {
		return totalComDesconto;
	}

	public void setTotalComDesconto(BigDecimal totalComDesconto) {
		this.totalComDesconto = totalComDesconto;
	}

	public boolean isEdicaoFechada() {
		return isEdicaoFechada;
	}

	public void setEdicaoFechada(boolean isEdicaoFechada) {
		this.isEdicaoFechada = isEdicaoFechada;
	}


	public Long getIdItemChamadaEncalheFornecedor() {
		return idItemChamadaEncalheFornecedor;
	}


	public void setIdItemChamadaEncalheFornecedor(
			Long idItemChamadaEncalheFornecedor) {
		this.idItemChamadaEncalheFornecedor = idItemChamadaEncalheFornecedor;
	}
	
	
	
}