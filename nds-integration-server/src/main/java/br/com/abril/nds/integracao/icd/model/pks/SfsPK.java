package br.com.abril.nds.integracao.icd.model.pks;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
	private String horaSolicitacao;

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
}