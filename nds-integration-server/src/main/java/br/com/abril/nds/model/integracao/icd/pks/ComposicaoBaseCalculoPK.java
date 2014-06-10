package br.com.abril.nds.model.integracao.icd.pks;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ComposicaoBaseCalculoPK implements Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	@Column(name = "COD_COMPOSICAO_BASE_CALCULO", nullable = false)
	private Long codComposicaoBaseCalculo;

	public Long getCodComposicaoBaseCalculo() {
		return codComposicaoBaseCalculo;
	}

	public void setCodComposicaoBaseCalculo(Long codComposicaoBaseCalculo) {
		this.codComposicaoBaseCalculo = codComposicaoBaseCalculo;
	}

}