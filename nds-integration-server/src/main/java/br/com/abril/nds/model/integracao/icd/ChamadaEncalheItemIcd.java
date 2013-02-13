package br.com.abril.nds.model.integracao.icd;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.abril.nds.model.integracao.icd.pks.ItemCEPK;

@Entity
@Table(name = "ITEM_CHAMADA_ENCALHE")
public class ChamadaEncalheItemIcd {

	@EmbeddedId
	private ItemCEPK itemCePK;
	
	@Column(name="NUM_DOCUMENTO_DCEN")
	private Long numeroDocumento;
	/*
	@ManyToOne
	ChamadaEncalheIcd chamadaEncalhe;*/

	/**
	 * Getters e Setters
	 */
	/*	
	public ChamadaEncalheIcd getChamadaEncalhe() {
		return chamadaEncalhe;
	}
	
	public void setChamadaEncalhe(ChamadaEncalheIcd chamadaEncalhe) {
		this.chamadaEncalhe = chamadaEncalhe;
	}*/

	public Long getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(Long numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public ItemCEPK getItemCePK() {
		return itemCePK;
	}

	public void setItemCePK(ItemCEPK itemCePK) {
		this.itemCePK = itemCePK;
	}
	
}
