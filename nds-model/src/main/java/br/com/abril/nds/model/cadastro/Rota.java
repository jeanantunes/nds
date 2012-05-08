package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	
	@Column(name = "CODIGO_ROTA")
	private String codigoRota;
	
	@Column(name = "DESCRICAO_ROTA")
	private String descricaoRota;
	
	@ManyToOne
	@JoinColumn(name = "ROTEIRO_ID")
	private Roteiro roteiro;
	
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

	public String getDescricaoRota() {
		return descricaoRota;
	}

	public void setDescricaoRota(String descricaoRota) {
		this.descricaoRota = descricaoRota;
	}

	public Roteiro getRoteiro() {
		return roteiro;
	}

	public void setRoteiro(Roteiro roteiro) {
		this.roteiro = roteiro;
	}
}