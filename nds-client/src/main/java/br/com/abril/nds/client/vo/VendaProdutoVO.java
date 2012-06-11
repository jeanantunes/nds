package br.com.abril.nds.client.vo;

import java.io.Serializable;

public class VendaProdutoVO implements Serializable{
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Long idVendaEncalhe;
	
	private String codigoBarras;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private Integer numeroEdicao;
	
	private String precoCapa;
	
	private Integer qntDisponivel;
	
	private Integer qntSolicitada;
	
	private String total;
	
	private String formaVenda;
	
	private String codBox;
	
	private String dataVencimentoDebito;
	
	private String dataVenda;
	
	private String nomeCota;
	
	private String numeroCota;


	/**
	 * @return the idVendaEncalhe
	 */
	public Long getIdVendaEncalhe() {
		return idVendaEncalhe;
	}

	/**
	 * @param idVendaEncalhe the idVendaEncalhe to set
	 */
	public void setIdVendaEncalhe(Long idVendaEncalhe) {
		this.idVendaEncalhe = idVendaEncalhe;
	}

	/**
	 * @return the codBox
	 */
	public String getCodBox() {
		return codBox;
	}

	/**
	 * @param codBox the codBox to set
	 */
	public void setCodBox(String codBox) {
		this.codBox = codBox;
	}

	/**
	 * @return the dataVencimentoDebito
	 */
	public String getDataVencimentoDebito() {
		return dataVencimentoDebito;
	}

	/**
	 * @param dataVencimentoDebito the dataVencimentoDebito to set
	 */
	public void setDataVencimentoDebito(String dataVencimentoDebito) {
		this.dataVencimentoDebito = dataVencimentoDebito;
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
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
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
	 * @return the qntDisponivel
	 */
	public Integer getQntDisponivel() {
		return qntDisponivel;
	}

	/**
	 * @param qntDisponivel the qntDisponivel to set
	 */
	public void setQntDisponivel(Integer qntDisponivel) {
		this.qntDisponivel = qntDisponivel;
	}

	/**
	 * @return the qntSolicitada
	 */
	public Integer getQntSolicitada() {
		return qntSolicitada;
	}

	/**
	 * @param qntSolicitada the qntSolicitada to set
	 */
	public void setQntSolicitada(Integer qntSolicitada) {
		this.qntSolicitada = qntSolicitada;
	}

	/**
	 * @return the precoCapa
	 */
	public String getPrecoCapa() {
		return precoCapa;
	}

	/**
	 * @param precoCapa the precoCapa to set
	 */
	public void setPrecoCapa(String precoCapa) {
		this.precoCapa = precoCapa;
	}

	/**
	 * @return the total
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(String total) {
		this.total = total;
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

	
	
	
}
