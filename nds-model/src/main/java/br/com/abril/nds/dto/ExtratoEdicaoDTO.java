package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ExtratoEdicaoDTO implements Serializable {
	
	/**
	 * Version UID
	 */
	private static final long serialVersionUID = 1L;

	private Long idMovimento;
	private Date dataMovimento;
	private String descMovimento;
	private BigDecimal qtdEdicaoEntrada ;
	private BigDecimal qtdEdicaoSaida ;
	private BigDecimal qtdParcial;
	
	public ExtratoEdicaoDTO(Long idMovimento, Date dataMovimento,
			String descMovimento, BigDecimal qtdEdicaoEntrada, BigDecimal qtdEdicaoSaida) {
		super();
		this.idMovimento = idMovimento;
		this.dataMovimento = dataMovimento;
		this.descMovimento = descMovimento;
		this.qtdEdicaoEntrada = qtdEdicaoEntrada;
		this.qtdEdicaoSaida = qtdEdicaoSaida;
	}

	public ExtratoEdicaoDTO(){
		
	}
	
	
	
	/**
	 * Obtém qtdEdicaoEntrada
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getQtdEdicaoEntrada() {
		return qtdEdicaoEntrada;
	}

	/**
	 * Atribuí qtdEdicaoEntrada
	 * @param qtdEdicaoEntrada 
	 */
	public void setQtdEdicaoEntrada(BigDecimal qtdEdicaoEntrada) {
		this.qtdEdicaoEntrada = qtdEdicaoEntrada;
	}

	/**
	 * Obtém qtdEdicaoSaida
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getQtdEdicaoSaida() {
		return qtdEdicaoSaida;
	}

	/**
	 * Atribuí qtdEdicaoSaida
	 * @param qtdEdicaoSaida 
	 */
	public void setQtdEdicaoSaida(BigDecimal qtdEdicaoSaida) {
		this.qtdEdicaoSaida = qtdEdicaoSaida;
	}

	/**
	 * Obtém qtdParcial
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getQtdParcial() {
		return qtdParcial;
	}

	/**
	 * Atribuí qtdParcial
	 * @param qtdParcial 
	 */
	public void setQtdParcial(BigDecimal qtdParcial) {
		this.qtdParcial = qtdParcial;
	}

	/**
	 * Obtém dataMovimento
	 *
	 * @return Date
	 */
	public Date getDataMovimento() {
		return dataMovimento;
	}

	/**
	 * Atribuí dataMovimento
	 * @param dataMovimento 
	 */
	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	/**
	 * Obtém descMovimento
	 *
	 * @return String
	 */
	public String getDescMovimento() {
		return descMovimento;
	}

	/**
	 * Atribuí descMovimento
	 * @param descMovimento 
	 */
	public void setDescMovimento(String descMovimento) {
		this.descMovimento = descMovimento;
	}
	

	/**
	 * Obtém idMovimento
	 *
	 * @return Long
	 */
	public Long getIdMovimento() {
		return idMovimento;
	}

	/**
	 * Atribuí idMovimento
	 * @param idMovimento 
	 */
	public void setIdMovimento(Long idMovimento) {
		this.idMovimento = idMovimento;
	}
	
}
