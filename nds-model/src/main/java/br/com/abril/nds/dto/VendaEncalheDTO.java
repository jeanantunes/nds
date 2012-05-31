package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

/**
 *  DTO que agrupa os parâmetros do sistema a serem exibidos/alterados na tela
 *  "Venda Encalhe".
 * 
 * @author Discover Technology
 */
@Exportable
public class VendaEncalheDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	@Export(label = "Data", alignment = Alignment.LEFT)
	private Date dataVenda;
	
	@Export(label = "Cota", alignment = Alignment.LEFT)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment = Alignment.LEFT)
	private String nomeCota;
	
	@Export(label = "Código", alignment = Alignment.LEFT)
	private String codigoProduto;
	
	@Export(label = "Produto", alignment = Alignment.LEFT)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment = Alignment.CENTER)
	private Integer numeroEdicao;
	
	@Export(label = "Preço Capa R$", alignment = Alignment.RIGHT)
	private BigDecimal precoCapa;
	
	@Export(label = "Quantidade", alignment = Alignment.CENTER)
	private Integer qntProduto;
	
	@Export(label = "Preço Desc. R$", alignment = Alignment.RIGHT)
	private BigDecimal precoDesconto;
	
	@Export(label = "Toral R$", alignment = Alignment.RIGHT)
	private BigDecimal valoTotalProduto;

	@Export(label = "Tipo Venda", alignment = Alignment.CENTER)
	private TipoVendaEncalhe tipoVendaEncalhe;

	/**
	 * @return the dataVenda
	 */
	public Date getDataVenda() {
		return dataVenda;
	}

	/**
	 * @param dataVenda the dataVenda to set
	 */
	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
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
	public Integer getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Integer numeroEdicao) {
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
	 * @return the qntProduto
	 */
	public Integer getQntProduto() {
		return qntProduto;
	}

	/**
	 * @param qntProduto the qntProduto to set
	 */
	public void setQntProduto(Integer qntProduto) {
		this.qntProduto = qntProduto;
	}

	/**
	 * @return the valoTotalProduto
	 */
	public BigDecimal getValoTotalProduto() {
		return valoTotalProduto;
	}

	/**
	 * @param valoTotalProduto the valoTotalProduto to set
	 */
	public void setValoTotalProduto(BigDecimal valoTotalProduto) {
		this.valoTotalProduto = valoTotalProduto;
	}

	/**
	 * @return the precoDesconto
	 */
	public BigDecimal getPrecoDesconto() {
		return precoDesconto;
	}

	/**
	 * @param precoDesconto the precoDesconto to set
	 */
	public void setPrecoDesconto(BigDecimal precoDesconto) {
		this.precoDesconto = precoDesconto;
	}

	/**
	 * @return the tipoVendaEncalhe
	 */
	public TipoVendaEncalhe getTipoVendaEncalhe() {
		return tipoVendaEncalhe;
	}

	/**
	 * @param tipoVendaEncalhe the tipoVendaEncalhe to set
	 */
	public void setTipoVendaEncalhe(TipoVendaEncalhe tipoVendaEncalhe) {
		this.tipoVendaEncalhe = tipoVendaEncalhe;
	}
	
	
	
}
