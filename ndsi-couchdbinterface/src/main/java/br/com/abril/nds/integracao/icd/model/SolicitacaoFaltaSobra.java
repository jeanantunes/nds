package br.com.abril.nds.integracao.icd.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "SOLICITACAO_FALTAS_SOBRAS")
public class SolicitacaoFaltaSobra {
	
	@Embeddable
	public class SfsPK implements Serializable {
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
	}

	
	@EmbeddedId
	private SfsPK sfsPK;	

	@Column(name = "COD_FORMA_SOLICITACAO", nullable=false)
	private String codigoForma;

	@Column(name = "COD_SITUACAO_SOLICITACAO", nullable=false)
	private String codigoSituacao;

	@OneToMany(mappedBy = "icdPK", cascade = CascadeType.ALL)
	private List<DetalheFaltaSobra> itens = new ArrayList<DetalheFaltaSobra>();

	/**
	 * @return the sfsPK
	 */
	public SfsPK getSfsPK() {
		return sfsPK;
	}

	/**
	 * @param sfsPK the sfsPK to set
	 */
	public void setSfsPK(SfsPK sfsPK) {
		this.sfsPK = sfsPK;
	}

	/**
	 * @return the codigoForma
	 */
	public String getCodigoForma() {
		return codigoForma;
	}

	/**
	 * @param codigoForma the codigoForma to set
	 */
	public void setCodigoForma(String codigoForma) {
		this.codigoForma = codigoForma;
	}

	/**
	 * @return the codigoSituacao
	 */
	public String getCodigoSituacao() {
		return codigoSituacao;
	}

	/**
	 * @param codigoSituacao the codigoSituacao to set
	 */
	public void setCodigoSituacao(String codigoSituacao) {
		this.codigoSituacao = codigoSituacao;
	}

	/**
	 * @return the itens
	 */
	public List<DetalheFaltaSobra> getItens() {
		return itens;
	}

	/**
	 * @param itens the itens to set
	 */
	public void setItens(List<DetalheFaltaSobra> itens) {
		this.itens = itens;
	}
	
	
}
