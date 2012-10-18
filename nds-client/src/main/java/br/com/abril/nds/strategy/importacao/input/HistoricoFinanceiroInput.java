package br.com.abril.nds.strategy.importacao.input;

import java.io.Serializable;
import java.math.BigDecimal;

public class HistoricoFinanceiroInput implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer numeroCota;
		
	private BigDecimal valorPendente; 
	
	private BigDecimal valorPostergado;
	
	private BigDecimal valorFuturo;
	
	private BigDecimal data;


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
	 * @return the valorPendente
	 */
	public BigDecimal getValorPendente() {
		return valorPendente;
	}

	/**
	 * @param valorPendente the valorPendente to set
	 */
	public void setValorPendente(BigDecimal valorPendente) {
		this.valorPendente = valorPendente;
	}

	/**
	 * @return the valorPostergado
	 */
	public BigDecimal getValorPostergado() {
		return valorPostergado;
	}

	/**
	 * @param valorPostergado the valorPostergado to set
	 */
	public void setValorPostergado(BigDecimal valorPostergado) {
		this.valorPostergado = valorPostergado;
	}

	/**
	 * @return the valorFuturo
	 */
	public BigDecimal getValorFuturo() {
		return valorFuturo;
	}

	/**
	 * @param valorFuturo the valorFuturo to set
	 */
	public void setValorFuturo(BigDecimal valorFuturo) {
		this.valorFuturo = valorFuturo;
	}

	/**
	 * @return the data
	 */
	public BigDecimal getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(BigDecimal data) {
		this.data = data;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
