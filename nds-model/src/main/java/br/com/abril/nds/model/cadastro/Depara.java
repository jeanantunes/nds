package br.com.abril.nds.model.cadastro;

import java.io.Serializable;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Classe que possui informações sobre depara .
 * 
 * @author Odemir
 *
 */
@Entity
@Table(name = "DEPARA")
@SequenceGenerator(name="DEPARA_SEQ", initialValue = 1, allocationSize = 1)
public class Depara implements Serializable {

	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6632216954435821598L;

	/**
	 * Construtor.
	 */
	public Depara() {
		
	}
	
	@Id
	@GeneratedValue(generator = "DEPARA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "fc", nullable = false)
	private String fc;
	
	@Column(name = "dinap", nullable = false)
	private String dinap;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	

	/**
	 * @return the fc
	 */
	public String getFc() {
		return this.fc;
	}

	/**
	 * @param fc the fc to set
	 */
	public void setFc(String fc) {
		this.fc = fc;
	}
	
	/**
	 * @return the fc
	 */
	public String getDinap() {
		return this.dinap;
	}

	/**
	 * @param fc the fc to set
	 */
	public void setDinap(String dinap) {
		this.dinap = dinap;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((fc == null) ? 0 : fc.hashCode());
		result = prime * result
				+ ((dinap == null) ? 0 : dinap.hashCode());
		
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Depara other = (Depara) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (fc == null) {
			if (other.fc != null)
				return false;
		} else if (!fc.equals(other.fc))
			return false;
		if (dinap == null) {
			if (other.dinap != null)
				return false;
		} else if (!dinap.equals(other.dinap))
			return false;
		
		return true;
	}
	
}
