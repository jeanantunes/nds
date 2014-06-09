package br.com.abril.nds.model.integracao.icd.pks;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BaseCalculoPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "COD_BASE_CALCULO", nullable = false)
	private Long codBaseCalculo;

	public Long getCodBaseCalculo() {
		return codBaseCalculo;
	}

	public void setCodBaseCalculo(Long codBaseCalculo) {
		this.codBaseCalculo = codBaseCalculo;
	}

}