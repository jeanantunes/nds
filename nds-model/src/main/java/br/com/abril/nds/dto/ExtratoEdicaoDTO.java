package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

public class ExtratoEdicaoDTO implements Serializable {
	
	/**
	 * Version UID
	 */
	private static final long serialVersionUID = 1L;

	public Date dataMovimento;

	public String descMovimento;
	
	public Long qtdEdicaoEntrada ;
	
	public Long qtdEdicaoSaida ;
	
	public Long qtdParcial;

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
	 * Obtém qtdEdicaoEntrada
	 *
	 * @return Long
	 */
	public Long getQtdEdicaoEntrada() {
		return qtdEdicaoEntrada;
	}

	/**
	 * Atribuí qtdEdicaoEntrada
	 * @param qtdEdicaoEntrada 
	 */
	public void setQtdEdicaoEntrada(Long qtdEdicaoEntrada) {
		this.qtdEdicaoEntrada = qtdEdicaoEntrada;
	}

	/**
	 * Obtém qtdEdicaoSaida
	 *
	 * @return Long
	 */
	public Long getQtdEdicaoSaida() {
		return qtdEdicaoSaida;
	}

	/**
	 * Atribuí qtdEdicaoSaida
	 * @param qtdEdicaoSaida 
	 */
	public void setQtdEdicaoSaida(Long qtdEdicaoSaida) {
		this.qtdEdicaoSaida = qtdEdicaoSaida;
	}

	/**
	 * Obtém qtdParcial
	 *
	 * @return Long
	 */
	public Long getQtdParcial() {
		return qtdParcial;
	}

	/**
	 * Atribuí qtdParcial
	 * @param qtdParcial 
	 */
	public void setQtdParcial(Long qtdParcial) {
		this.qtdParcial = qtdParcial;
	}
	
	
	
	
}
