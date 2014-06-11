package br.com.abril.nds.client.vo;

import java.io.Serializable;

public class ResultadoServicoVO implements Serializable {

	private static final long serialVersionUID = 3646943680195208770L;

	private Long id;
	
	private String codigo;

	private String descricao;
	
	private String taxa;
		
	private String baseCalculo;
	
	private String percentualCalculoBase;
	
	private String periodicidade;
	
	private String tipoCobranca;
	
	private Integer diaSemana;
	
	private Integer diaMes;
	
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

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * @return the diaSemana
	 */
	public Integer getDiaSemana() {
		return diaSemana;
	}

	/**
	 * @param diaSemana the diaSemana to set
	 */
	public void setDiaSemana(Integer diaSemana) {
		this.diaSemana = diaSemana;
	}

	/**
	 * @return the diaMes
	 */
	public Integer getDiaMes() {
		return diaMes;
	}

	/**
	 * @param diaMes the diaMes to set
	 */
	public void setDiaMes(Integer diaMes) {
		this.diaMes = diaMes;
	}
	
}
