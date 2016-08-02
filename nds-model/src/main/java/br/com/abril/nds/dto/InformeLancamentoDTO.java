package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class InformeLancamentoDTO implements Serializable {

	private static final long serialVersionUID = -1894841345731437679L;
	
	@Export(label = "Sequencia")
	private Integer sequencia;
	
	private Long idProdutoEdicao;
	
	@Export(label = "Codigo Produto")
	private String codigoProduto;
	
	@Export(label = "Nome Produto")
	private String nomeProduto;
	
	@Export(label = "Numero Edicao")
	private Long numeroEdicao;
	
	@Export(label = "Chamada Capa")
	private String chamadaCapa;
	
	@Export(label = "Codigo De Barras")
	private String codigoDeBarras;
	
	private BigDecimal precoVenda;
	
	@Export(label = "Preco Venda")
	private String precoVendaFormatado;

	public Integer getSequencia() {
		return sequencia;
	}

	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
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

	public String getChamadaCapa() {
		return chamadaCapa;
	}

	public void setChamadaCapa(String chamadaCapa) {
		this.chamadaCapa = chamadaCapa;
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
		this.precoVendaFormatado = CurrencyUtil.formatarValor(precoVenda);
	}
	
	public String getPrecoVendaFormatado() {
		return precoVendaFormatado;
	}

	public void setPrecoVendaFormatado(String precoVendaFormatado) {
		this.precoVendaFormatado = precoVendaFormatado;
	}

}
