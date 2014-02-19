package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TRIBUTACAO")
public class Tributacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4671205416746773116L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="tributo")
	private String tributo;
	
	@Column(name="base_calculo")
	private BigDecimal baseCalculo;
	
	@Column(name="cst_a")
	private String cstA;
	
	@Column(name="cst")
	private String cst;
	
	@Column(name="valor_aliquota")
	private BigDecimal valorAliquota;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTributo() {
		return tributo;
	}

	public void setTributo(String tributo) {
		this.tributo = tributo;
	}

	public BigDecimal getBaseCalculo() {
		return baseCalculo;
	}

	public void setBaseCalculo(BigDecimal baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	public String getCstA() {
		return cstA;
	}

	public void setCstA(String cstA) {
		this.cstA = cstA;
	}

	public String getCst() {
		return cst;
	}

	public void setCst(String cst) {
		this.cst = cst;
	}

	public BigDecimal getValorAliquota() {
		return valorAliquota;
	}

	public void setValorAliquota(BigDecimal valorAliquota) {
		this.valorAliquota = valorAliquota;
	}
	
}