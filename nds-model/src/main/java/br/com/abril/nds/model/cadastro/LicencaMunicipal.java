package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Classe que representa a informação da licença municipal
 * 
 * @author francisco.garcia
 *
 */
@Embeddable
public class LicencaMunicipal implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "TIPO_LICENCA_MUNICIPAL_ID")
	private TipoLicencaMunicipal tipoLicencaMunicipal;
	
	@Column(name = "NUMERO_LICENCA")
	private String numeroLicenca;
	
	@Column(name = "NOME_LICENCA")
	private String nomeLicenca;

	public TipoLicencaMunicipal getTipoLicencaMunicipal() {
		return tipoLicencaMunicipal;
	}

	public void setTipoLicencaMunicipal(TipoLicencaMunicipal tipoLicencaMunicipal) {
		this.tipoLicencaMunicipal = tipoLicencaMunicipal;
	}

	public String getNumeroLicenca() {
		return numeroLicenca;
	}

	public void setNumeroLicenca(String numeroLicenca) {
		this.numeroLicenca = numeroLicenca;
	}

	public String getNomeLicenca() {
		return nomeLicenca;
	}

	public void setNomeLicenca(String nomeLicenca) {
		this.nomeLicenca = nomeLicenca;
	}

}
