package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class EncalheCotaDTO implements Serializable {

	private static final long serialVersionUID = 2186060384671120600L;
	
	private Long sequencia;
	
	@Export(label = "Código", fontSize=9)
	private String codigoProduto;

	@Export(label = "Produto", fontSize=9, widthPercent=30)
	private String nomeProduto;

	@Export(label = "Edição", fontSize=9)
	private Long numeroEdicao;

	@Export(label = "Preço Capa R$", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA, fontSize=9)
	private BigDecimal precoCapa;

	@Export(label = "Preço c/ Desc. R$", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA_QUATRO_CASAS, fontSize=9)
	private BigDecimal precoComDesconto;
	
	@Export(label = "Encalhe", alignment = Alignment.CENTER, fontSize=9)
	private BigInteger encalhe;
	
	@Export(label = "Fornecedor", fontSize=9, widthPercent=7)
	private String nomeFornecedor;

	@Export(label = "Total R$", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA_QUATRO_CASAS, fontSize=9)
	private BigDecimal total;

	private BigDecimal desconto;
	
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

	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}

	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}

	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto == null ? BigDecimal.ZERO : precoComDesconto.setScale(4, RoundingMode.HALF_EVEN);
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total != null ? total.setScale(4, RoundingMode.HALF_EVEN) : BigDecimal.ZERO;
	}

	public BigInteger getEncalhe() {
		return encalhe;
	}

	public void setEncalhe(BigInteger encalhe) {
		this.encalhe = encalhe;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	
	//Retorna Object porque caso seja null tem que apresentar o descrito "Postergado", 
	//e o campo não pode ser do tipo String devido a ordenação do mesmo.
	@Export(label = "Sequência", fontSize=9, widthPercent=7)
	public Object getSequencia() {
		return (sequencia == null ? "Postergado" : sequencia);
	}

	public void setSequencia(Long sequencia) {
		this.sequencia = sequencia;
	}
}