package br.com.abril.nds.client.vo;

import java.io.Serializable;

/**
 * 
 * Classe responsável por armazenar os valores referente a 
 * consulta de resumo de edições expedidas agrupadas por produto e box
 * 
 * @author Discover Technology
 *
 */
public class ResumoExpedicaoVO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String dataLancamento;
	
	private String codigoProduto;
	
	private String descricaoProduto;
	
	private String edicaoProduto;
	
	private String precoCapa;
	
	private String reparte;
	
	private String valorFaturado;
	
	private String codigoBox;
	
	private String descricaoBox;
	
	private String qntProduto;
	
	private String qntDiferenca;
	
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
	 * @return the dataLancamento
	 */
	public String getDataLancamento() {
		return dataLancamento;
	}
	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
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
	 * @return the descricaoProduto
	 */
	public String getDescricaoProduto() {
		return descricaoProduto;
	}
	/**
	 * @param descricaoProduto the descricaoProduto to set
	 */
	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}
	/**
	 * @return the edicaoProduto
	 */
	public String getEdicaoProduto() {
		return edicaoProduto;
	}
	/**
	 * @param edicaoProduto the edicaoProduto to set
	 */
	public void setEdicaoProduto(String edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
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
	 * @return the reparte
	 */
	public String getReparte() {
		return reparte;
	}
	/**
	 * @param reparte the reparte to set
	 */
	public void setReparte(String reparte) {
		this.reparte = reparte;
	}
	/**
	 * @return the valorFaturado
	 */
	public String getValorFaturado() {
		return valorFaturado;
	}
	/**
	 * @param valorFaturado the valorFaturado to set
	 */
	public void setValorFaturado(String valorFaturado) {
		this.valorFaturado = valorFaturado;
	}
	/**
	 * @return the codigoBox
	 */
	public String getCodigoBox() {
		return codigoBox;
	}
	/**
	 * @param codigoBox the codigoBox to set
	 */
	public void setCodigoBox(String codigoBox) {
		this.codigoBox = codigoBox;
	}
	/**
	 * @return the descricaoBox
	 */
	public String getDescricaoBox() {
		return descricaoBox;
	}
	/**
	 * @param descricaoBox the descricaoBox to set
	 */
	public void setDescricaoBox(String descricaoBox) {
		this.descricaoBox = descricaoBox;
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
	 * @return the qntDiferenca
	 */
	public String getQntDiferenca() {
		return qntDiferenca;
	}
	/**
	 * @param qntDiferenca the qntDiferenca to set
	 */
	public void setQntDiferenca(String qntDiferenca) {
		this.qntDiferenca = qntDiferenca;
	}
	
	
}

