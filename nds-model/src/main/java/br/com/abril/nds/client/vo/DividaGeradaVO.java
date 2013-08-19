package br.com.abril.nds.client.vo;

import java.io.Serializable;

public class DividaGeradaVO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String box;
	private String rota;
	private String roteiro;
	private String numeroCota;
	private String nomeCota;
	private String dataVencimento;
	private String dataEmissao;
	private String valor;
	private String tipoCobranca;
	private String suportaEmail;
	private String vias;
	private String nossoNumero;

	
	/**
	 * @return the nossoNumero
	 */
	public String getNossoNumero() {
		return nossoNumero;
	}
	/**
	 * @param nossoNumero the nossoNumero to set
	 */
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}
	/**
	 * @return the vias
	 */
	public String getVias() {
		return vias;
	}
	/**
	 * @param vias the vias to set
	 */
	public void setVias(String vias) {
		this.vias = vias;
	}
	/**
	 * @return the box
	 */
	public String getBox() {
		return box;
	}
	/**
	 * @param box the box to set
	 */
	public void setBox(String box) {
		this.box = box;
	}
	/**
	 * @return the rota
	 */
	public String getRota() {
		return rota;
	}
	/**
	 * @param rota the rota to set
	 */
	public void setRota(String rota) {
		this.rota = rota;
	}
	/**
	 * @return the roteiro
	 */
	public String getRoteiro() {
		return roteiro;
	}
	/**
	 * @param roteiro the roteiro to set
	 */
	public void setRoteiro(String roteiro) {
		this.roteiro = roteiro;
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
	 * @return the dataVencimento
	 */
	public String getDataVencimento() {
		return dataVencimento;
	}
	/**
	 * @param dataVencimento the dataVencimento to set
	 */
	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	/**
	 * @return the dataEmissao
	 */
	public String getDataEmissao() {
		return dataEmissao;
	}
	/**
	 * @param dataEmissao the dataEmissao to set
	 */
	public void setDataEmissao(String dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	/**
	 * @return the tipoCobranca
	 */
	public String getTipoCobranca() {
		return tipoCobranca;
	}
	/**
	 * @param tipoCobranca the tipoCobranca to set
	 */
	public void setTipoCobranca(String tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}
	/**
	 * @return the suportaEmail
	 */
	public String getSuportaEmail() {
		return suportaEmail;
	}
	/**
	 * @param suportaEmail the suportaEmail to set
	 */
	public void setSuportaEmail(String suportaEmail) {
		this.suportaEmail = suportaEmail;
	}
	
	
}
