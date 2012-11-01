package br.com.abril.nds.integracao.icd.model;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "DETALHE_FALTAS_SOBRAS")
public class DetalheFaltaSobra {

	@EmbeddedId
	private IcdPK icdPK;	
	
	@Column(name = "COD_SITUACAO_ACERTO", nullable = false)
	private Integer codigoAcerto;

	/**
	 * @return the icdPK
	 */
	public IcdPK getIcdPK() {
		return icdPK;
	}

	/**
	 * @param icdPK the icdPK to set
	 */
	public void setIcdPK(IcdPK icdPK) {
		this.icdPK = icdPK;
	}

	/**
	 * @return the codigoAcerto
	 */
	public Integer getCodigoAcerto() {
		return codigoAcerto;
	}

	/**
	 * @param codigoAcerto the codigoAcerto to set
	 */
	public void setCodigoAcerto(Integer codigoAcerto) {
		this.codigoAcerto = codigoAcerto;
	}

	

	
}
