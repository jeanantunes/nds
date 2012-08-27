package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class VendaEncalheVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idVenda;

	@Export(label = "Data", alignment = Alignment.LEFT)
	private String dataVenda;
	
	@Export(label = "Cota", alignment = Alignment.LEFT)
	private String numeroCota;
	
	@Export(label = "Nome", alignment = Alignment.LEFT)
	private String nomeCota;
	
	@Export(label = "Código", alignment = Alignment.LEFT)
	private String codigoProduto;
	
	@Export(label = "Produto", alignment = Alignment.LEFT)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment = Alignment.CENTER)
	private String numeroEdicao;
	
	@Export(label = "Quantidade", alignment = Alignment.CENTER)
	private String qntProduto;
	
	@Export(label = "Preço Desc. R$", alignment = Alignment.RIGHT)
	private String precoDesconto;
	
	@Export(label = "Total R$", alignment = Alignment.RIGHT)
	private String valoTotalProduto;

	@Export(label = "Tipo Venda", alignment = Alignment.CENTER)
	private String tipoVendaEncalhe;

	private String formaVenda;
	
	private String qntDisponivelProduto;
	
	private String codigoBarras;
	
	private Boolean edicaoExclusaoItem;
	
	/**
	 * @return the edicaoExclusaoItem
	 */
	public Boolean getEdicaoExclusaoItem() {
		return edicaoExclusaoItem;
	}

	/**
	 * @param edicaoExclusaoItem the edicaoExclusaoItem to set
	 */
	public void setEdicaoExclusaoItem(Boolean edicaoExclusaoItem) {
		this.edicaoExclusaoItem = edicaoExclusaoItem;
	}

	/**
	 * @return the formaVenda
	 */
	public String getFormaVenda() {
		return formaVenda;
	}

	/**
	 * @param formaVenda the formaVenda to set
	 */
	public void setFormaVenda(String formaVenda) {
		this.formaVenda = formaVenda;
	}

	/**
	 * @return the qntDisponivelProduto
	 */
	public String getQntDisponivelProduto() {
		return qntDisponivelProduto;
	}

	/**
	 * @param qntDisponivelProduto the qntDisponivelProduto to set
	 */
	public void setQntDisponivelProduto(String qntDisponivelProduto) {
		this.qntDisponivelProduto = qntDisponivelProduto;
	}

	/**
	 * @return the codigoBarras
	 */
	public String getCodigoBarras() {
		return codigoBarras;
	}

	/**
	 * @param codigoBarras the codigoBarras to set
	 */
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	/**
	 * @return the idVenda
	 */
	public Long getIdVenda() {
		return idVenda;
	}

	/**
	 * @param idVenda the idVenda to set
	 */
	public void setIdVenda(Long idVenda) {
		this.idVenda = idVenda;
	}

	/**
	 * @return the dataVenda
	 */
	public String getDataVenda() {
		return dataVenda;
	}

	/**
	 * @param dataVenda the dataVenda to set
	 */
	public void setDataVenda(String dataVenda) {
		this.dataVenda = dataVenda;
	}

	/**
	 * @return the numeroCota
	 */
	public String getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(String numeroCota) {
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
	public String getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * @return the qntProduto
	 */
	public String getQntProduto() {
		return qntProduto;
	}

	/**
	 * @param qntProduto the qntProduto to set
	 */
	public void setQntProduto(String qntProduto) {
		this.qntProduto = qntProduto;
	}

	/**
	 * @return the precoDesconto
	 */
	public String getPrecoDesconto() {
		return precoDesconto;
	}

	/**
	 * @param precoDesconto the precoDesconto to set
	 */
	public void setPrecoDesconto(String precoDesconto) {
		this.precoDesconto = precoDesconto;
	}

	/**
	 * @return the valoTotalProduto
	 */
	public String getValoTotalProduto() {
		return valoTotalProduto;
	}

	/**
	 * @param valoTotalProduto the valoTotalProduto to set
	 */
	public void setValoTotalProduto(String valoTotalProduto) {
		this.valoTotalProduto = valoTotalProduto;
	}

	/**
	 * @return the tipoVendaEncalhe
	 */
	public String getTipoVendaEncalhe() {
		return tipoVendaEncalhe;
	}

	/**
	 * @param tipoVendaEncalhe the tipoVendaEncalhe to set
	 */
	public void setTipoVendaEncalhe(String tipoVendaEncalhe) {
		this.tipoVendaEncalhe = tipoVendaEncalhe;
	}
	
	
}
