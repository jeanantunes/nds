package br.com.abril.nds.model.integracao.icd.pks;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.integracao.icd.ChamadaEncalheIcd;

@Embeddable
public class CEItemPK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "NUM_CHAMADA_ENCALHE_CHEN", nullable = false)
	ChamadaEncalheIcd chamadaEncalhe;
	
	@Column(name = "NUM_ITEM_ITCE", nullable = false)
	private Long numeroItem;

	/**
	 * Getters e Setters
	 */
	public Long getNumeroItem() {
		return numeroItem;
	}

	public ChamadaEncalheIcd getChamadaEncalhe() {
		return chamadaEncalhe;
	}

	public void setChamadaEncalhe(ChamadaEncalheIcd chamadaEncalhe) {
		this.chamadaEncalhe = chamadaEncalhe;
	}

	public void setNumeroItem(Long numeroItem) {
		this.numeroItem = numeroItem;
	}

}
