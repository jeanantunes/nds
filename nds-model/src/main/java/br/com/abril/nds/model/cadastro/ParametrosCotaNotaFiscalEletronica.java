package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ParametrosCotaNotaFiscalEletronica implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column( name="EXIGE_NF_E", nullable = true)
	private Boolean exigeNotaFiscalEletronica;
	
	@Column( name="EMAIL_NF_E", nullable = true)
	private String emailNotaFiscalEletronica;

	@Column( name="CONTRIBUINTE_ICMS", nullable = true)
	private Boolean contribuinteICMS;
	
	/**
	 * @return the exigeNotaFiscalEletronica
	 */
	public Boolean isExigeNotaFiscalEletronica() {
		return exigeNotaFiscalEletronica;
	}

	/**
	 * @param exigeNotaFiscalEletronica the exigeNotaFiscalEletronica to set
	 */
	public void setExigeNotaFiscalEletronica(Boolean exigeNotaFiscalEletronica) {
		this.exigeNotaFiscalEletronica = exigeNotaFiscalEletronica;
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

	public Boolean isContribuinteICMS() {
		return contribuinteICMS;
	}

	public void setContribuinteICMS(Boolean contribuinteICMS) {
		this.contribuinteICMS = contribuinteICMS;
	}
}
