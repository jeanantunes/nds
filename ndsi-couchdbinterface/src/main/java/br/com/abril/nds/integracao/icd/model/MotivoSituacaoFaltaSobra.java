package br.com.abril.nds.integracao.icd.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "MOTIVO_SITUACAO_FALTAS_SOBRAS")
public class MotivoSituacaoFaltaSobra {

	@Embeddable
	public class MfsPK implements Serializable {

		private static final long serialVersionUID = 1L;

		@Column(name = "COD_DISTRIBUIDOR")
		private Long codigoDistribuidor;

		@Column(name = "DAT_SOLICITACAO")
		private Date dataSolicitacao;

		@Column(name = "HRA_SOLICITACAO")
		private Time horaSolicitacao;
		
		@Column(name = "NUM_SEQUENCIA_DETALHE")
		private Integer numeroSequencia;

		/**
		 * @return the codigoDistribuidor
		 */
		public Long getCodigoDistribuidor() {
			return codigoDistribuidor;
		}

		/**
		 * @param codigoDistribuidor the codigoDistribuidor to set
		 */
		public void setCodigoDistribuidor(Long codigoDistribuidor) {
			this.codigoDistribuidor = codigoDistribuidor;
		}

		/**
		 * @return the dataSolicitacao
		 */
		public Date getDataSolicitacao() {
			return dataSolicitacao;
		}

		/**
		 * @param dataSolicitacao the dataSolicitacao to set
		 */
		public void setDataSolicitacao(Date dataSolicitacao) {
			this.dataSolicitacao = dataSolicitacao;
		}

		/**
		 * @return the horaSolicitacao
		 */
		public Time getHoraSolicitacao() {
			return horaSolicitacao;
		}

		/**
		 * @param horaSolicitacao the horaSolicitacao to set
		 */
		public void setHoraSolicitacao(Time horaSolicitacao) {
			this.horaSolicitacao = horaSolicitacao;
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
				
		
	}
	
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
