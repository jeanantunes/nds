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
@Table(name = "MOTIVO_SITUACAO_FALTAS_SOBRAS")
public class MotivoSituacaoFaltaSobra {

	@EmbeddedId
	private IcdPK icdPK;	
	
	@Column(name = "NUM_SEQUENCIA_MOTIVO")
	private Integer numeroMotivo;
	
	@Column(name = "DSC_MOTIVO_SITUACAO")
	private String descricaoMotivo;
	
	@Column(name = "COD_ORIGEM_MOTIVO")
	private Integer codigoMotivo;

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
	 * @return the numeroMotivo
	 */
	public Integer getNumeroMotivo() {
		return numeroMotivo;
	}

	/**
	 * @param numeroMotivo the numeroMotivo to set
	 */
	public void setNumeroMotivo(Integer numeroMotivo) {
		this.numeroMotivo = numeroMotivo;
	}

	/**
	 * @return the descricaoMotivo
	 */
	public String getDescricaoMotivo() {
		return descricaoMotivo;
	}

	/**
	 * @param descricaoMotivo the descricaoMotivo to set
	 */
	public void setDescricaoMotivo(String descricaoMotivo) {
		this.descricaoMotivo = descricaoMotivo;
	}

	/**
	 * @return the codigoMotivo
	 */
	public Integer getCodigoMotivo() {
		return codigoMotivo;
	}

	/**
	 * @param codigoMotivo the codigoMotivo to set
	 */
	public void setCodigoMotivo(Integer codigoMotivo) {
		this.codigoMotivo = codigoMotivo;
	}


}
