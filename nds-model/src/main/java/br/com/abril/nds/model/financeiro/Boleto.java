package br.com.abril.nds.model.financeiro;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.com.abril.nds.model.cadastro.TipoCobranca;

/**
 * @author luiz.marcili
 * @version 1.0
 * @created 02-mar-2012 09:25:00
 */
@Entity
@DiscriminatorValue(value = "BOLETO")
public class Boleto extends Cobranca {

	@Column(name = "DIGITO_NOSSO_NUMERO", nullable = true)
	protected String digitoNossoNumero;
	
	@Column(name = "NOSSO_NUMERO_COMPLETO", nullable = true, unique = true)
	protected String nossoNumeroCompleto;
	
	public Boleto() {
		this.tipoCobranca = TipoCobranca.BOLETO;
	}
	
	public String getDigitoNossoNumero() {
		return digitoNossoNumero;
	}

	public void setDigitoNossoNumero(String digitoNossoNumero) {
		this.digitoNossoNumero = digitoNossoNumero;
	}

	public String getNossoNumeroCompleto() {
		return nossoNumeroCompleto;
	}

	public void setNossoNumeroCompleto(String nossoNumeroCompleto) {
		this.nossoNumeroCompleto = nossoNumeroCompleto;
	}
	
}