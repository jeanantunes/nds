package br.com.abril.nds.model.integracao.icd;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.abril.nds.model.integracao.icd.pks.CEItemPK;

@Entity
@Table(name = "ITEM_CHAMADA_ENCALHE")
public class ChamadaEncalheIcdItem {

	@EmbeddedId
	private CEItemPK ceItemPK;
	
	@Column(name = "DATA_RECOLHIMENTO_ITCE", nullable = false)
	private Date dataRecolhimento;

	/**
	 * 
	 */
	public CEItemPK getCeItemPK() {
		return ceItemPK;
	}

	public void setCeItemPK(CEItemPK ceItemPK) {
		this.ceItemPK = ceItemPK;
	}

	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

}
