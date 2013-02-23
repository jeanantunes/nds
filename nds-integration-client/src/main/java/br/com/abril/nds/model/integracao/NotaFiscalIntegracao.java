package br.com.abril.nds.model.integracao;

import javax.persistence.Transient;

import br.com.abril.nds.model.fiscal.nota.NotaFiscal;

public class NotaFiscalIntegracao extends NotaFiscal {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transient
	private String tipoDocumento;

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
}
