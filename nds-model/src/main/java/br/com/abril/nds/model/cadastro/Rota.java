package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ROTA")
@SequenceGenerator(name="ROTA_SEQ", initialValue = 1, allocationSize = 1)
public class Rota implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7234522705455824338L;

	@Id
	@GeneratedValue(generator = "ROTA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name="CODIGO_ROTA")
	private String codigoRota;
	
	@OneToMany
	@JoinColumn( name="ROTA_ID")
	private List<Roteiro> roteiros;

	public List<Roteiro> getRoteiros() {
		return roteiros;
	}

	public void setRoteiros(List<Roteiro> roteiros) {
		this.roteiros = roteiros;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoRota() {
		return codigoRota;
	}

	public void setCodigoRota(String codigoRota) {
		this.codigoRota = codigoRota;
	} 
	

}
