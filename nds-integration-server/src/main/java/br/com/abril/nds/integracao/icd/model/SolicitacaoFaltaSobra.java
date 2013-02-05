package br.com.abril.nds.integracao.icd.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.abril.nds.integracao.icd.model.pks.SfsPK;


@Entity
@Table(name = "SOLICITACAO_FALTAS_SOBRAS")
public class SolicitacaoFaltaSobra {
	
	@EmbeddedId
	private SfsPK sfsPK;	

	@Column(name = "COD_FORMA_SOLICITACAO", nullable=false)
	private String codigoForma;

	@Column(name = "COD_SITUACAO_SOLICITACAO", nullable=false)
	private String codigoSituacao;
	

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumns(value = { 
			@JoinColumn (name = "COD_DISTRIBUIDOR", referencedColumnName = "COD_DISTRIBUIDOR", insertable=false, updatable=false)
			, @JoinColumn (name = "DAT_SOLICITACAO", referencedColumnName = "DAT_SOLICITACAO", insertable=false, updatable=false)
			, @JoinColumn (name = "HRA_SOLICITACAO", referencedColumnName = "HRA_SOLICITACAO", insertable=false, updatable=false)
	})
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
