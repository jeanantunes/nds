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
		private String horaSolicitacao;
		
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
		public String getHoraSolicitacao() {
			return horaSolicitacao;
		}

		/**
		 * @param horaSolicitacao the horaSolicitacao to set
		 */
		public void setHoraSolicitacao(String horaSolicitacao) {
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
		
	@Column(name = "COD_PUBLICACAO_ADABAS", nullable = true)    
	private Long codigoPublicacaoAdabas;
	
	@Column(name = "NUM_EDICAO", nullable = true)
	private Long numeroEdicao;
	
	@Column(name = "QTD_SOLICITADA", nullable = true)
	private Long qtdSolicitada;
	
	@Column(name = "NUM_DOCUMENTO_ORIGEM", nullable = true)
	private Long numeroDocumentoOrigem;
		
	@Column(name = "VLR_UNITARIO", nullable = true)
	private Double valorUnitario;
	
	@Column(name = "PCT_DESCONTO", nullable = true)
	private Double pctDesconto;
	
	@Column(name = "NUM_DOCUMENTO_ACERTO", nullable = true)
	private Long numeroDocumentoAcerto;
	
	@Column(name = "DAT_EMISSAO_DOCUMENTO_ACERTO", nullable = true)
	private Date dataEmissaoDocumentoAcerto;
	
	@Column(name = "COD_USUARIO_ACERTO", nullable = true)
	private String codigoUsuarioAcerto;	
	
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



	/**
	 * @return the codigoPublicacaoAdabas
	 */
	public Long getCodigoPublicacaoAdabas() {
		return codigoPublicacaoAdabas;
	}



	/**
	 * @param codigoPublicacaoAdabas the codigoPublicacaoAdabas to set
	 */
	public void setCodigoPublicacaoAdabas(Long codigoPublicacaoAdabas) {
		this.codigoPublicacaoAdabas = codigoPublicacaoAdabas;
	}



	/**
	 * @return the numeroEdicao
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}



	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}



	/**
	 * @return the qtdSolicitada
	 */
	public Long getQtdSolicitada() {
		return qtdSolicitada;
	}



	/**
	 * @param qtdSolicitada the qtdSolicitada to set
	 */
	public void setQtdSolicitada(Long qtdSolicitada) {
		this.qtdSolicitada = qtdSolicitada;
	}



	/**
	 * @return the numeroDocumentoOrigem
	 */
	public Long getNumeroDocumentoOrigem() {
		return numeroDocumentoOrigem;
	}



	/**
	 * @param numeroDocumentoOrigem the numeroDocumentoOrigem to set
	 */
	public void setNumeroDocumentoOrigem(Long numeroDocumentoOrigem) {
		this.numeroDocumentoOrigem = numeroDocumentoOrigem;
	}



	/**
	 * @return the valorUnitario
	 */
	public Double getValorUnitario() {
		return valorUnitario;
	}



	/**
	 * @param valorUnitario the valorUnitario to set
	 */
	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}



	/**
	 * @return the pctDesconto
	 */
	public Double getPctDesconto() {
		return pctDesconto;
	}



	/**
	 * @param pctDesconto the pctDesconto to set
	 */
	public void setPctDesconto(Double pctDesconto) {
		this.pctDesconto = pctDesconto;
	}



	/**
	 * @return the numeroDocumentoAcerto
	 */
	public Long getNumeroDocumentoAcerto() {
		return numeroDocumentoAcerto;
	}



	/**
	 * @param numeroDocumentoAcerto the numeroDocumentoAcerto to set
	 */
	public void setNumeroDocumentoAcerto(Long numeroDocumentoAcerto) {
		this.numeroDocumentoAcerto = numeroDocumentoAcerto;
	}



	/**
	 * @return the dataEmissaoDocumentoAcerto
	 */
	public Date getDataEmissaoDocumentoAcerto() {
		return dataEmissaoDocumentoAcerto;
	}



	/**
	 * @param dataEmissaoDocumentoAcerto the dataEmissaoDocumentoAcerto to set
	 */
	public void setDataEmissaoDocumentoAcerto(Date dataEmissaoDocumentoAcerto) {
		this.dataEmissaoDocumentoAcerto = dataEmissaoDocumentoAcerto;
	}



	/**
	 * @return the codigoUsuarioAcerto
	 */
	public String getCodigoUsuarioAcerto() {
		return codigoUsuarioAcerto;
	}


	/**
	 * @param codigoUsuarioAcerto the codigoUsuarioAcerto to set
	 */
	public void setCodigoUsuarioAcerto(String codigoUsuarioAcerto) {
		this.codigoUsuarioAcerto = codigoUsuarioAcerto;
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

}
