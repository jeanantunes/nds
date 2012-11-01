package br.com.abril.nds.integracao.icd.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DETALHE_FALTAS_SOBRAS")
public class DetalheFaltaSobra {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "COD_SITUACAO_ACERTO", nullable = false)
	private String codigoAcerto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoAcerto() {
		return codigoAcerto;
	}

	public void setCodigoAcerto(String codigoAcerto) {
		this.codigoAcerto = codigoAcerto;
	}


	
}
