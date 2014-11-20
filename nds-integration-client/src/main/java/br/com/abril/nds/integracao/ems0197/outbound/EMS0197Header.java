package br.com.abril.nds.integracao.ems0197.outbound;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0197Header implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tipoRegistro;
	private Long idCota;
	private String numeroCota;
	private String nomePDV;
	private Date dataLctoDistrib;
	private static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
	private String codDistribuidor;
	
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
				+ "|" + this.getFormatedDate();

		return tipoRegistro;
	}
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	
	public String getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(String numeroCota) {
		this.numeroCota = numeroCota;
	}
	public String getNomePDV() {
		return nomePDV;
	}
	public void setNomePDV(String nomePDV) {
		this.nomePDV = nomePDV;
	}
	
	public Long getIdCota() {
		return idCota;
	}
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}
	public Date getDataLctoDistrib() {
		return dataLctoDistrib;
	}
	public void setDataLctoDistrib(Date dataLctoDistrib) {
		this.dataLctoDistrib = dataLctoDistrib;
	}
	
	public String getFormatedDate() {
		return sdf.format(this.dataLctoDistrib);
	}
	public String getCodDistribuidor() {
		return codDistribuidor;
	}
	public void setCodDistribuidor(String codDistribuidor) {
		this.codDistribuidor = codDistribuidor;
	}
	
}