package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ImpostoProduto extends Imposto implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 536918061402194564L;

	protected BigDecimal valorIsento;
	
	protected BigDecimal valorOutros;
	
	protected BigDecimal valorDebito;
	
	protected BigDecimal valorCredito;
	
	protected String tipoBaseCalculo;

	/**
	 * @return the cst
	 */
	public String getCst() {
		return cst;
	}

	/**
	 * @param cst the cst to set
	 */
	public void setCst(String cst) {
		this.cst = cst;
	}

	/**
	 * @return the valorBaseCalculo
	 */
	public BigDecimal getValorBaseCalculo() {
		return valorBaseCalculo;
	}

	/**
	 * @param valorBaseCalculo the valorBaseCalculo to set
	 */
	public void setValorBaseCalculo(BigDecimal valorBaseCalculo) {
		this.valorBaseCalculo = valorBaseCalculo;
	}

	/**
	 * @return the aliquota
	 */
	public BigDecimal getAliquota() {
		return aliquota;
	}

	/**
	 * @param aliquota the aliquota to set
	 */
	public void setAliquota(BigDecimal aliquota) {
		this.aliquota = aliquota;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the valorIsento
	 */
	public BigDecimal getValorIsento() {
		return valorIsento;
	}

	/**
	 * @param valorIsento the valorIsento to set
	 */
	public void setValorIsento(BigDecimal valorIsento) {
		this.valorIsento = valorIsento;
	}

	/**
	 * @return the valorOutros
	 */
	public BigDecimal getValorOutros() {
		return valorOutros;
	}

	/**
	 * @param valorOutros the valorOutros to set
	 */
	public void setValorOutros(BigDecimal valorOutros) {
		this.valorOutros = valorOutros;
	}

	/**
	 * @return the valorDebito
	 */
	public BigDecimal getValorDebito() {
		return valorDebito;
	}

	/**
	 * @param valorDebito the valorDebito to set
	 */
	public void setValorDebito(BigDecimal valorDebito) {
		this.valorDebito = valorDebito;
	}

	/**
	 * @return the valorCredito
	 */
	public BigDecimal getValorCredito() {
		return valorCredito;
	}

	/**
	 * @param valorCredito the valorCredito to set
	 */
	public void setValorCredito(BigDecimal valorCredito) {
		this.valorCredito = valorCredito;
	}

	/**
	 * @return the tipoBaseCalculo
	 */
	public String getTipoBaseCalculo() {
		return tipoBaseCalculo;
	}

	/**
	 * @param tipoBaseCalculo the tipoBaseCalculo to set
	 */
	public void setTipoBaseCalculo(String tipoBaseCalculo) {
		this.tipoBaseCalculo = tipoBaseCalculo;
	}

}
