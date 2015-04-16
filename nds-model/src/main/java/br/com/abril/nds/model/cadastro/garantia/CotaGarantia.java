package br.com.abril.nds.model.cadastro.garantia;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.TipoGarantia;


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
public abstract class CotaGarantia implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1885134367490899850L;

	@Id
	@GeneratedValue(generator="COTA_GARANTIA_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA")
	private Date data;
	
	@Enumerated(EnumType.STRING)
	@Column(name="TIPO_GARANTIA")
	private TipoGarantia tipoGarantia;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TipoGarantia getTipoGarantia() {
		return tipoGarantia;
	}

	public void setTipoGarantia(TipoGarantia tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}
	
}