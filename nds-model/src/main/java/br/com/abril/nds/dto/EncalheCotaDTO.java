package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class EncalheCotaDTO implements Serializable {

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
		this.precoComDesconto = precoComDesconto;
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
		this.total = total;
	}

	private static final long serialVersionUID = 2186060384671120600L;
	
	@Export(label = "Código")
	private String codigoProduto;

	@Export(label = "Produto")
	private String nomeProduto;

	@Export(label = "Edição")
	private Long numeroEdicao;

	@Export(label = "Preço Capa R$", alignment = Alignment.RIGHT)
	private BigDecimal precoCapa;

	@Export(label = "Preço c/ Desc. R$", alignment = Alignment.RIGHT)
	private BigDecimal precoComDesconto;
	
	@Export(label = "Encalhe", alignment = Alignment.CENTER)
	private BigInteger encalhe;
	
	@Export(label = "Fornecedor")
	private String nomeFornecedor;

	@Export(label = "Toral R$", alignment = Alignment.RIGHT)
	private BigDecimal total;

	private BigDecimal desconto;

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

}
