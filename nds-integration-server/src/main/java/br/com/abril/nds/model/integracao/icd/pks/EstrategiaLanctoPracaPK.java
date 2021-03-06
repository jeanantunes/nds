package br.com.abril.nds.model.integracao.icd.pks;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EstrategiaLanctoPracaPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Column(name = "COD_ESTRATEGIA", nullable = false)
	private Long codEstrategia;

	@Column(name = "COD_LANCTO_EDICAO", nullable = false)
	private Long codLanctoEdicao;
	
	public Long getCodEstrategia() {
		return codEstrategia;
	}

	public void setCodEstrategia(Long codEstrategia) {
		this.codEstrategia = codEstrategia;
	}
	
	public Long getCodLanctoEdicao() {
		return codLanctoEdicao;
	}

	public void setCodLanctoEdicao(Long codLanctoEdicao) {
		this.codLanctoEdicao = codLanctoEdicao;
	}


}