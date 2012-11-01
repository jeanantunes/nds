package br.com.abril.nds.integracao.icd.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "MOTIVO_SITUACAO_FALTAS_SOBRAS")
public class MotivoSituacaoFaltaSobra {

	@EmbeddedId
	private IcdPK icdPK;
	
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

	@Column(name = "NUM_SEQUENCIA_MOTIVO")
	private Integer numeroSequencia;
	
	@Column(name = "DSC_MOTIVO_SITUACAO")
	private String descricaoMotivo;
	
	@Column(name = "COD_ORIGEM_MOTIVO")
	private String codigoMotivo;

	

	/**
	 * @return the numeroSequencia
	 */
	public Integer getNumeroSequencia() {
		return numeroSequencia;
	}

	/**
	 * @param numeroSequencia the numeroSequencia to set
	 */
	public void setNumeroSequencia(Integer numeroSequencia) {
		this.numeroSequencia = numeroSequencia;
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
	public String getCodigoMotivo() {
		return codigoMotivo;
	}

	/**
	 * @param codigoMotivo the codigoMotivo to set
	 */
	public void setCodigoMotivo(String codigoMotivo) {
		this.codigoMotivo = codigoMotivo;
	}
	
	
}
