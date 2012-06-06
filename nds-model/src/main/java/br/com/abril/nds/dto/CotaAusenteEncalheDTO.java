package br.com.abril.nds.dto;

import java.io.Serializable;

public class CotaAusenteEncalheDTO implements Serializable {

	private static final long serialVersionUID = -5167121794665878284L;
	
	private Integer numeroCota;

	private String colaboradorName;
	
	private String boxName;
	
	private String roteiroName;
	
	private String rotaName;
	
	private String acao = "";
	
	private String check;
	
	/**
	 * Construtor Padrão
	 */
	public CotaAusenteEncalheDTO() {
		
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
	 * @return the colaboradorName
	 */
	public String getColaboradorName() {
		return colaboradorName;
	}

	/**
	 * @param colaboradorName the colaboradorName to set
	 */
	public void setColaboradorName(String colaboradorName) {
		this.colaboradorName = colaboradorName;
	}

	/**
	 * @return the boxName
	 */
	public String getBoxName() {
		return boxName;
	}

	/**
	 * @param boxName the boxName to set
	 */
	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	/**
	 * @return the roteiroName
	 */
	public String getRoteiroName() {
		return roteiroName;
	}

	/**
	 * @param roteiroName the roteiroName to set
	 */
	public void setRoteiroName(String roteiroName) {
		this.roteiroName = roteiroName;
	}

	/**
	 * @return the rotaName
	 */
	public String getRotaName() {
		return rotaName;
	}

	/**
	 * @param rotaName the rotaName to set
	 */
	public void setRotaName(String rotaName) {
		this.rotaName = rotaName;
	}

	/**
	 * @return the acao
	 */
	public String getAcao() {
		return acao;
	}

	/**
	 * @param acao the acao to set
	 */
	public void setAcao(String acao) {
		this.acao = acao;
	}

	/**
	 * @return the check
	 */
	public String getCheck() {
		return check;
	}

	/**
	 * @param check the check to set
	 */
	public void setCheck(String check) {
		this.check = check;
	}
	
}
