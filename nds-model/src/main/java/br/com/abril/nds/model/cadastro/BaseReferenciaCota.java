package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BASE_REFERENCIA_COTA")
@SequenceGenerator(name="BASE_REFERENCIA_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class BaseReferenciaCota implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "BASE_REFERENCIA_COTA_SEQ")
	@Column(name = "ID")
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name="INICIO_PERIODO")
	private Date inicioPeriodo;
	
	@Temporal(TemporalType.DATE)
	@Column(name="FINAL_PERIODO")
	private Date finalPeriodo;
	
	@OneToMany(mappedBy = "baseReferenciaCota", cascade={CascadeType.REMOVE})
	private Set<ReferenciaCota> referenciasCota;

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
	 * @return the inicioPeriodo
	 */
	public Date getInicioPeriodo() {
		return inicioPeriodo;
	}

	/**
	 * @param inicioPeriodo the inicioPeriodo to set
	 */
	public void setInicioPeriodo(Date inicioPeriodo) {
		this.inicioPeriodo = inicioPeriodo;
	}
	/**
	 * @return the referenciasCota
	 */
	public Set<ReferenciaCota> getReferenciasCota() {
		return referenciasCota;
	}

	/**
	 * @param referenciasCota the referenciasCota to set
	 */
	public void setReferenciasCota(Set<ReferenciaCota> referenciasCota) {
		this.referenciasCota = referenciasCota;
	}

	/**
	 * @return the finalPeriodo
	 */
	public Date getFinalPeriodo() {
		return finalPeriodo;
	}

	/**
	 * @param finalPeriodo the finalPeriodo to set
	 */
	public void setFinalPeriodo(Date finalPeriodo) {
		this.finalPeriodo = finalPeriodo;
	}
	
	
}
