package br.com.abril.nds.model.integracao.icd;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "BASE_CALCULO")
public class BaseCalculo {
	 
	@Id
	@Column(name = "COD_BASE_CALCULO")
	private Long codBaseCalculo;
	
	@Transient
	private String tipoDocumento;
	
	@Transient
	private String baseDeDados;
	
	@Transient
	private String usuarioBaseDeDados;
	
	@Transient
	private Long codigoDistribuidor;
	
	@Column(name = "NOM_BASE_CALCULO")
	private String nomBaseCalculo;
	
	@Column(name = "DSC_BASE_CALCULO")
	private String dscBaseCalculo;

	/**
	 * Getters e Setters 
	 */
	
	public Long getCodBaseCalculo() {
		return codBaseCalculo;
	}

	public void setCodBaseCalculo(Long codBaseCalculo) {
		this.codBaseCalculo = codBaseCalculo;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getBaseDeDados() {
		return baseDeDados;
	}

	public void setBaseDeDados(String baseDeDados) {
		this.baseDeDados = baseDeDados;
	}

	public String getUsuarioBaseDeDados() {
		return usuarioBaseDeDados;
	}

	public void setUsuarioBaseDeDados(String usuarioBaseDeDados) {
		this.usuarioBaseDeDados = usuarioBaseDeDados;
	}

	public Long getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	public void setCodigoDistribuidor(Long codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}

	public String getNomBaseCalculo() {
		return nomBaseCalculo;
	}

	public void setNomBaseCalculo(String nomBaseCalculo) {
		this.nomBaseCalculo = nomBaseCalculo;
	}

	public String getDscBaseCalculo() {
		return dscBaseCalculo;
	}

	public void setDscBaseCalculo(String dscBaseCalculo) {
		this.dscBaseCalculo = dscBaseCalculo;
	}
}
