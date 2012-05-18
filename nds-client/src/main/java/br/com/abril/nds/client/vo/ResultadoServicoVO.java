package br.com.abril.nds.client.vo;

import java.io.Serializable;

public class ResultadoServicoVO implements Serializable {

	private static final long serialVersionUID = 3646943680195208770L;

	private String codigo;

	private String descricao;
	
	private String taxa;
	
	private String isento;
	
	private String baseCalculo;
	
	private String percentualCalculoBase;
	
	private String periodicidade;
	
	public ResultadoServicoVO() {
		
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the taxa
	 */
	public String getTaxa() {
		return taxa;
	}

	/**
	 * @param taxa the taxa to set
	 */
	public void setTaxa(String taxa) {
		this.taxa = taxa;
	}

	/**
	 * @return the isento
	 */
	public String isIsento() {
		return isento;
	}

	/**
	 * @param isento the isento to set
	 */
	public void setIsento(String isento) {
		this.isento = isento;
	}

	/**
	 * @return the baseCalculo
	 */
	public String getBaseCalculo() {
		return baseCalculo;
	}

	/**
	 * @param baseCalculo the baseCalculo to set
	 */
	public void setBaseCalculo(String baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	/**
	 * @return the percentualCalculoBase
	 */
	public String getPercentualCalculoBase() {
		return percentualCalculoBase;
	}

	/**
	 * @param percentualCalculoBase the percentualCalculoBase to set
	 */
	public void setPercentualCalculoBase(String percentualCalculoBase) {
		this.percentualCalculoBase = percentualCalculoBase;
	}

	/**
	 * @return the periodicidade
	 */
	public String getPeriodicidade() {
		return periodicidade;
	}

	/**
	 * @param periodicidade the periodicidade to set
	 */
	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}
	
}
