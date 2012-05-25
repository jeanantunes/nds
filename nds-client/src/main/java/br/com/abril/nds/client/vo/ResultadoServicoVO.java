package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class ResultadoServicoVO implements Serializable {

	private static final long serialVersionUID = 3646943680195208770L;

	private Long id;
	
	private String codigo;

	private String descricao;
	
	private BigDecimal taxa;
		
	private String baseCalculo;
	
	private Float percentualCalculoBase;
	
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
	public BigDecimal getTaxa() {
		return taxa;
	}

	/**
	 * @param taxa the taxa to set
	 */
	public void setTaxa(BigDecimal taxa) {
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
	public Float getPercentualCalculoBase() {
		return percentualCalculoBase;
	}

	/**
	 * @param percentualCalculoBase the percentualCalculoBase to set
	 */
	public void setPercentualCalculoBase(Float percentualCalculoBase) {
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
	
}
