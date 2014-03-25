package br.com.abril.nds.model.financeiro;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import br.com.abril.nds.model.cadastro.TipoCobranca;

/**
 * @author luiz.marcili
 * @version 1.0
 * @created 02-mar-2012 09:25:00
 */
@Entity
@DiscriminatorValue(value = "BOLETO")
public class Boleto extends Cobranca {
	
	@Transient
	private boolean recebeCobrancaEmail;
	
	public Boleto() {
		this.tipoCobranca = TipoCobranca.BOLETO;
	}

	/**
	 * @return the recebeCobrancaEmail
	 */
	public boolean isRecebeCobrancaEmail() {
		return recebeCobrancaEmail;
	}

	/**
	 * @param recebeCobrancaEmail the recebeCobrancaEmail to set
	 */
	public void setRecebeCobrancaEmail(boolean recebeCobrancaEmail) {
		this.recebeCobrancaEmail = recebeCobrancaEmail;
	}
}