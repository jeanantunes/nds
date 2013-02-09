package br.com.abril.nds.integracao.icd.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.abril.nds.integracao.icd.model.pks.MfsPK;

@Entity
@Table(name = "MOTIVO_SITUACAO_FALTAS_SOBRAS")
public class MotivoSituacaoFaltaSobra {

	@EmbeddedId
	private MfsPK mfsPK;

	@Column(name = "NUM_SEQUENCIA_MOTIVO", nullable = false)
	private Integer numeroSequencia;
	
	@Column(name = "DSC_MOTIVO_SITUACAO")
	private String descricaoMotivo;
	
	@Column(name = "COD_ORIGEM_MOTIVO")
	private String codigoMotivo;
	
	/**
	 * @return the mfsPK
	 */
	public MfsPK getMfsPK() {
		return mfsPK;
	}

	/**
	 * @param mfsPK the mfsPK to set
	 */
	public void setMfsPK(MfsPK mfsPK) {
		this.mfsPK = mfsPK;
	}

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
