package br.com.abril.nds.model.integracao.icd;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.abril.nds.model.integracao.icd.pks.CEPK;

@Entity
@Table(name = "CHAMADA_ENCALHE")
public class ChamadaEncalheIcd {

	@EmbeddedId
	private CEPK cePK;	
	
	@Column(name = "DATA_EMISSAO_CHEN", nullable = false)
	private Date dataEmissao;
	
	@OneToMany
	List<ChamadaEncalheIcdItem> chamadaEncalheItens;

	/**
	 * Getters e Setters 
	 */
	
	public CEPK getCePK() {
		return cePK;
	}

	public void setCePK(CEPK cePK) {
		this.cePK = cePK;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	
	
}
