package br.com.abril.nds.model.fiscal.nota;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class TribIPI {
	/**
	 * Construtor padrão.
	 */
	public TribIPI() {
		
	}
	
	@XmlElement(name="CST")
	@Transient
	private String cst;
	

	/**
	 * vBC
	 */
	@Column(name="VALOR_BASE_CALCULO_IPI", scale=4, precision=15, nullable=true)
	@XmlElement(name="vBC")
	private BigDecimal valorBaseCalculo;
	
	/**
	 * pIPI
	 */
	@Column(name="VALOR_ALIQUOTA_IPI", scale=4, precision=15, nullable=true)
	@XmlElement(name="pIPI")
	private BigDecimal valorAliquota;

	/**
	 * vIPI
	 */
	@Column(name="VALOR_IPI", scale=4, precision=15, nullable=true)
	@XmlElement(name="vIPI")
	private BigDecimal valorIPI;

	public BigDecimal getValorBaseCalculo() {
		return valorBaseCalculo;
	}

	public void setValorBaseCalculo(BigDecimal valorBaseCalculo) {
		this.valorBaseCalculo = valorBaseCalculo;
	}

	public BigDecimal getValorAliquota() {
		return valorAliquota;
	}

	public void setValorAliquota(BigDecimal valorAliquota) {
		this.valorAliquota = valorAliquota;
	}

	public BigDecimal getValorIPI() {
		return valorIPI;
	}

	public void setValorIPI(BigDecimal valorIPI) {
		this.valorIPI = valorIPI;
	}

	public String getCst() {
		return cst;
	}

	public void setCst(String cst) {
		this.cst = cst;
	}
	
	
}
