package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
	
	@Column(name="base_calculo")
	private BigDecimal baseCalculo;
	
	@OneToOne
	@JoinColumn(name="tributo_aliquota_id")
	private TributoAliquota tributoAliquota;

	@OneToOne
	@JoinColumn(name="cst_id")
	private CST cst;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getBaseCalculo() {
		return baseCalculo;
	}

	public void setBaseCalculo(BigDecimal baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	public TributoAliquota getTributoAliquota() {
		return tributoAliquota;
	}

	public void setTributoAliquota(TributoAliquota tributoAliquota) {
		this.tributoAliquota = tributoAliquota;
	}

	public CST getCst() {
		return cst;
	}

	public void setCst(CST cst) {
		this.cst = cst;
	}
	
}