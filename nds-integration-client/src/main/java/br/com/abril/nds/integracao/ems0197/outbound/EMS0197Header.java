package br.com.abril.nds.integracao.ems0197.outbound;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0197Header implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tipoRegistro;
	private String numeroCota;
	private String nomePDV;
	private String dataLctoDistrib;
	
	
	/**
	 * 
	 * FIXME: O tamanho esta "erroneamente" fixado em 100 posições porque este
	 * a API FixedFormat4J gera apenas arquivos do tipo posicional (e 
	 * este arquivo é do tipo que utiliza delimitadores).
	 * O tamanho de 100 posições deve ser suficiente para este tipo de linha.
	 * 
	 * @return
	 */
	@Field(offset = 1, length = 100)
	public String getTipoRegistro() {
		this.tipoRegistro = "1" 
				+ "|" + this.numeroCota 
				+ "|" + this.nomePDV
				+ "|" + this.dataLctoDistrib;

		return tipoRegistro;
	}
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	public String getCodigoCota() {
		return numeroCota;
	}
	public void setCodigoCota(String codigoCota) {
		this.numeroCota = codigoCota;
	}
	public String getNomePDV() {
		return nomePDV;
	}
	public void setNomePDV(String nomePDV) {
		this.nomePDV = nomePDV;
	}
	public String getDataLctoDistrib() {
		return dataLctoDistrib;
	}
	public void setDataLctoDistrib(String dataLctoDistrib) {
		this.dataLctoDistrib = dataLctoDistrib;
	}
}