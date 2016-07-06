package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class VendaEncalheVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idVenda;

	@Export(label = "Data", alignment = Alignment.LEFT,widthPercent=7)
	private String dataVenda;
	
	@Export(label = "Cota", alignment = Alignment.LEFT,widthPercent=5)
	private String numeroCota;
	
	@Export(label = "Nome", alignment = Alignment.LEFT,widthPercent=26)
	private String nomeCota;
	
	@Export(label = "Código", alignment = Alignment.LEFT,widthPercent=7)
	private String codigoProduto;
	
	@Export(label = "Produto", alignment = Alignment.LEFT,widthPercent=15)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment = Alignment.CENTER,widthPercent=5)
	private String numeroEdicao;
	
	@Export(label = "Qtde.", alignment = Alignment.CENTER,widthPercent=4)
	private String qntProduto;
	
	@Export(label = "Preço Desc. R$", alignment = Alignment.RIGHT,widthPercent=6)
	private String precoDesconto;
	
	@Export(label = "Total R$", alignment = Alignment.RIGHT,widthPercent=7)
	private String valoTotalProduto;

	@Export(label = "Tipo Venda", alignment = Alignment.CENTER,widthPercent=9)
	private String tipoVendaEncalhe;

	@Export(label = "Usuário", alignment = Alignment.LEFT,widthPercent=9)
	private String nomeUsuario;
	
	private String formaVenda;
	
	private String qntDisponivelProduto;
	
	private String codigoBarras;
	
	private Boolean edicaoExclusaoItem;
	
	private TipoVendaEncalhe tipoVenda;
	
	private FormaComercializacao formaComercializacao;
	
	private String comercializacao;
	
	private boolean produtoContaFirme;
	
	public boolean isProdutoContaFirme() {
		return produtoContaFirme;
	}

	public void setProdutoContaFirme(boolean produtoContaFirme) {
		this.produtoContaFirme = produtoContaFirme;
	}

	/**
	 * @return the tipoVenda
	 */
	public TipoVendaEncalhe getTipoVenda() {
		return tipoVenda;
	}

	/**
	 * @param tipoVenda the tipoVenda to set
	 */
	public void setTipoVenda(TipoVendaEncalhe tipoVenda) {
		this.tipoVenda = tipoVenda;
	}

	/**
	 * @return the formaComercializacao
	 */
	public FormaComercializacao getFormaComercializacao() {
		return formaComercializacao;
	}

	/**
	 * @param formaComercializacao the formaComercializacao to set
	 */
	public void setFormaComercializacao(FormaComercializacao formaComercializacao) {
		this.formaComercializacao = formaComercializacao;
	}

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

	/**
	 * @return the nomeUsuario
	 */
	public String getNomeUsuario() {
		return nomeUsuario;
	}

	/**
	 * @param nomeUsuario the nomeUsuario to set
	 */
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getComercializacao() {
		return comercializacao;
	}

	public void setComercializacao(String comercializacao) {
		this.comercializacao = comercializacao;
	}
}
