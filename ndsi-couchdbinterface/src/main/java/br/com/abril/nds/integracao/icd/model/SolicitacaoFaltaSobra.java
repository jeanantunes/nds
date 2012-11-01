package br.com.abril.nds.integracao.icd.model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

	@OneToMany
	private List<DetalheFaltaSobra> itens = new ArrayList<DetalheFaltaSobra>();
	
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
