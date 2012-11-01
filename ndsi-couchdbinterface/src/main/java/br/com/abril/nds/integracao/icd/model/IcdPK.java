package br.com.abril.nds.integracao.icd.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Embeddable
public class IcdPK implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "COD_DISTRIBUIDOR")
	private Integer codigoDistribuidor;

	@Column(name = "DAT_SOLICITACAO")
	private Date dataSolicitacao;

	@Column(name = "HRA_SOLICITACAO")
	private Time horaSolicitacao;

	/**
	 * @return the codigoDistribuidor
	 */
	public Integer getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	/**
	 * @param codigoDistribuidor the codigoDistribuidor to set
	 */
	public void setCodigoDistribuidor(Integer codigoDistribuidor) {
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
