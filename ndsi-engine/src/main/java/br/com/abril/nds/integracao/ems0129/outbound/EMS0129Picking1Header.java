package br.com.abril.nds.integracao.ems0129.outbound;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0129Picking1Header implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tipoRegistro;
	
	private Integer codigoCota;
	
	private String nomeCota;
	
	
	@Field(offset = 1, length = 1, paddingChar = '1')
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	
	@Field(offset = 2, length = 5)
	public Integer getCodigoCota() {
		return codigoCota;
	}
	
	@Field(offset = 7, length = 51)
	public String getNomeCota() {
		return nomeCota;
	}
	
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	
	public void setCodigoCota(Integer codigoCota) {
		this.codigoCota = codigoCota;
	}
	
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}	
}
