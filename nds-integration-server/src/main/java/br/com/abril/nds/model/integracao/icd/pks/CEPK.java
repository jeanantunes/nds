package br.com.abril.nds.model.integracao.icd.pks;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CEPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "NUM_CHAMADA_ENCALHE_CHEN", nullable = false)
	private Long numeroChamadaEncalhe;

	public Long getNumeroChamadaEncalhe() {
		return numeroChamadaEncalhe;
	}

	public void setNumeroChamadaEncalhe(Long numeroChamadaEncalhe) {
		this.numeroChamadaEncalhe = numeroChamadaEncalhe;
	}

}