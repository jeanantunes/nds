package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ParametrosCotaNotaFiscalEletronica implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column( name="EMITE_NF_E", nullable = true)
	private Boolean emiteNotaFiscalEletronica;
	
	@Column( name="EMAIL_NF_E", nullable = true)
	private String emailNotaFiscalEletronica;

	/**
	 * @return the emiteNotaFiscalEletronica
	 */
	public Boolean getEmiteNotaFiscalEletronica() {
		return emiteNotaFiscalEletronica;
	}

	/**
	 * @param emiteNotaFiscalEletronica the emiteNotaFiscalEletronica to set
	 */
	public void setEmiteNotaFiscalEletronica(Boolean emiteNotaFiscalEletronica) {
		this.emiteNotaFiscalEletronica = emiteNotaFiscalEletronica;
	}

	/**
	 * @return the emailNotaFiscalEletronica
	 */
	public String getEmailNotaFiscalEletronica() {
		return emailNotaFiscalEletronica;
	}

	/**
	 * @param emailNotaFiscalEletronica the emailNotaFiscalEletronica to set
	 */
	public void setEmailNotaFiscalEletronica(String emailNotaFiscalEletronica) {
		this.emailNotaFiscalEletronica = emailNotaFiscalEletronica;
	}
	
	
}
