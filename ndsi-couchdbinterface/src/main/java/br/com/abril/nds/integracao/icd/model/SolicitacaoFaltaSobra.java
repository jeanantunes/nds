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
@Table(name = "SOLICITACAO_FALTAS_SOBRAS")
public class SolicitacaoFaltaSobra {
	
	@EmbeddedId
	private IcdPK icdPK;	

	@Column(name = "COD_FORMA_SOLICITACAO")
	private Integer codigoForma;

	@Column(name = "COD_SITUACAO_SOLICITACAO")
	private Integer codigoSituacao;

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
	 * @return the codigoForma
	 */
	public Integer getCodigoForma() {
		return codigoForma;
	}

	/**
	 * @param codigoForma the codigoForma to set
	 */
	public void setCodigoForma(Integer codigoForma) {
		this.codigoForma = codigoForma;
	}

	/**
	 * @return the codigoSituacao
	 */
	public Integer getCodigoSituacao() {
		return codigoSituacao;
	}

	/**
	 * @param codigoSituacao the codigoSituacao to set
	 */
	public void setCodigoSituacao(Integer codigoSituacao) {
		this.codigoSituacao = codigoSituacao;
	}

	
}
