package br.com.abril.nds.integracao.icd.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.FetchMode;

@Entity
@Table(name = "DETALHE_FALTAS_SOBRAS")
public class DetalheFaltaSobra {

	@Embeddable
	public class DfsPK implements Serializable {
		/**
		 * 
		 */
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
	private DfsPK dfsPK;	
	
	@Column(name = "COD_SITUACAO_ACERTO", nullable = false)
	private String codigoAcerto;
	
	@Column(name = "COD_TIPO_ACERTO", nullable = true)
	private Integer codigoTipoAcerto;
	
	
	@ManyToOne(fetch=FetchType.LAZY)	
	@JoinColumns(value = { 
			@JoinColumn (name = "COD_DISTRIBUIDOR", referencedColumnName = "COD_DISTRIBUIDOR", insertable=false, updatable=false)
			, @JoinColumn (name = "DAT_SOLICITACAO", referencedColumnName = "DAT_SOLICITACAO", insertable=false, updatable=false)
			, @JoinColumn (name = "HRA_SOLICITACAO", referencedColumnName = "HRA_SOLICITACAO", insertable=false, updatable=false)
			, @JoinColumn (name = "NUM_SEQUENCIA_DETALHE", referencedColumnName = "NUM_SEQUENCIA_DETALHE", insertable=false, updatable=false)			
	})
	private MotivoSituacaoFaltaSobra motivoSituacaoFaltaSobra;


	/**
	 * @return the dfsPK
	 */
	public DfsPK getDfsPK() {
		return dfsPK;
	}


	/**
	 * @param dfsPK the dfsPK to set
	 */
	public void setDfsPK(DfsPK dfsPK) {
		this.dfsPK = dfsPK;
	}


	/**
	 * @return the codigoAcerto
	 */
	public String getCodigoAcerto() {
		return codigoAcerto;
	}


	/**
	 * @param codigoAcerto the codigoAcerto to set
	 */
	public void setCodigoAcerto(String codigoAcerto) {
		this.codigoAcerto = codigoAcerto;
	}


	/**
	 * @return the motivoSituacaoFaltaSobra
	 */
	public MotivoSituacaoFaltaSobra getMotivoSituacaoFaltaSobra() {
		return motivoSituacaoFaltaSobra;
	}


	/**
	 * @param motivoSituacaoFaltaSobra the motivoSituacaoFaltaSobra to set
	 */
	public void setMotivoSituacaoFaltaSobra(
			MotivoSituacaoFaltaSobra motivoSituacaoFaltaSobra) {
		this.motivoSituacaoFaltaSobra = motivoSituacaoFaltaSobra;
	}


	/**
	 * @return the codigoTipoAcerto
	 */
	public Integer getCodigoTipoAcerto() {
		return codigoTipoAcerto;
	}


	/**
	 * @param codigoTipoAcerto the codigoTipoAcerto to set
	 */
	public void setCodigoTipoAcerto(Integer codigoTipoAcerto) {
		this.codigoTipoAcerto = codigoTipoAcerto;
	}
	
}
