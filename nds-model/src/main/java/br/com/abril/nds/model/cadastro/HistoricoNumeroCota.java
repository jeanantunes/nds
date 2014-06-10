package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class HistoricoNumeroCota implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private HistoricoNumeroCotaPK pk;
	
	@Column(name="NUMERO_COTA")
	private Integer numeroCota;
	
	@ManyToOne
	@JoinColumn(name="ID_COTA",referencedColumnName="ID",insertable=false, updatable=false)
	private Cota cota;
	
	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public HistoricoNumeroCotaPK getPk() {
		return pk;
	}

	public void setPk(HistoricoNumeroCotaPK pk) {
		this.pk = pk;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}	
}
