package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "REFERENCIA_COTA",
	   uniqueConstraints = { @UniqueConstraint(columnNames = {"BASE_REFERENCIA_COTA_ID", "COTA_ID" })}
)
@SequenceGenerator(name="REFERENCIA_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class ReferenciaCota implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "REFERENCIA_COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "BASE_REFERENCIA_COTA_ID")
	private BaseReferenciaCota baseReferenciaCota;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@Column(name="PERCENTUAL")
	private BigDecimal percentual;

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
	 * @return the baseReferenciaCota
	 */
	public BaseReferenciaCota getBaseReferenciaCota() {
		return baseReferenciaCota;
	}

	/**
	 * @param baseReferenciaCota the baseReferenciaCota to set
	 */
	public void setBaseReferenciaCota(BaseReferenciaCota baseReferenciaCota) {
		this.baseReferenciaCota = baseReferenciaCota;
	}

	/**
	 * @return the cota
	 */
	public Cota getCota() {
		return cota;
	}

	/**
	 * @param cota the cota to set
	 */
	public void setCota(Cota cota) {
		this.cota = cota;
	}

	/**
	 * @return the percentual
	 */
	public BigDecimal getPercentual() {
		return percentual;
	}

	/**
	 * @param percentual the percentual to set
	 */
	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}
	
	
}
