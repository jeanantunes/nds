package br.com.abril.nds.model.cadastro.garantia;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

import br.com.abril.nds.model.cadastro.Cota;


/**
 * 
 * @author Diego Fernandes
 * @version 1.0
 */

@Entity
@Table(name = "COTA_GARANTIA")
@SequenceGenerator(name="COTA_GARANTIA_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class CotaGarantia implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1885134367490899850L;


	@Id
	@GeneratedValue(generator="COTA_GARANTIA_SEQ")
	@Column(name="ID")
	private Long id;
	
	@JsonIgnore
	@OneToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA")
	private Calendar data;
	


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}
}
