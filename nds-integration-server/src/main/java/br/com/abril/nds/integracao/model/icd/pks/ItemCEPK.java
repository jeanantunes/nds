package br.com.abril.nds.integracao.model.icd.pks;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ItemCEPK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="NUM_CHAMADA_ENCALHE_CHEN", nullable = false)
	private Long numeroChamadaEncalhe;

	@Column(name="NUM_ITEM_ITCE", nullable = false)
	private Long numeroItem;
	
	/**
	 * Getters e Setters
	 */

	public Long getNumeroChamadaEncalhe() {
		return numeroChamadaEncalhe;
	}

	public void setNumeroChamadaEncalhe(Long numeroChamadaEncalhe) {
		this.numeroChamadaEncalhe = numeroChamadaEncalhe;
	}

	public Long getNumeroItem() {
		return numeroItem;
	}

	public void setNumeroItem(Long numeroItem) {
		this.numeroItem = numeroItem;
	}
	
}
