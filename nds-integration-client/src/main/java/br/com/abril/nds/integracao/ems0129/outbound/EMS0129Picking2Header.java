package br.com.abril.nds.integracao.ems0129.outbound;

import java.io.Serializable;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0129Picking2Header implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idCota;
	
	private String tipoRegistro;
	
	private String codigoCota;
	
	private String nomeCotaComCpfCnpj;
	
	private Date data;
	
	private String nomeFantasia;
	
	private String tipoNota;
	
	private String filler;
	
	private String campoFixo1;
	
	private String campoFixo2;
	
	private String campoFixo3;
		
	private String separador1;
	
	private String separador2;
	
	private String separador3;
	
	private String separador4;
	
	private String separador5;
	
	private String separador6;
	
	private String separador7;
	
	private String separador8;
	
	private String separador9;
	
	private String separador10;
	
	
	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	@Field(offset = 1, length = 1, paddingChar = '1')
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	
	@Field(offset = 2, length = 1, paddingChar = ';')
	public String getSeparador1() {
		return separador1;
	}
	
	@Field(offset = 3, length = 4, align = Align.RIGHT)
	public String getCodigoCota() {
		return codigoCota;
	}
	
	@Field(offset = 7, length = 1, paddingChar = ';')
	public String getSeparador2() {
		return separador2;
	}
	
	@Field(offset = 8, length = 40)
	public String getNomeCotaComCpfCnpj() {
		return nomeCotaComCpfCnpj;
	}
	
	@Field(offset = 48, length = 1, paddingChar = ';')
	public String getSeparador3() {
		return separador3;
	}
	
	@Field(offset = 49, length = 8, align = Align.RIGHT)
	@FixedFormatPattern("ddMMyyyy")
	public Date getData() {
		return data;
	}
	
	@Field(offset = 57, length = 1, paddingChar = ';')
	public String getSeparador4() {
		return separador4;
	}
	
	@Field(offset = 58, length = 20)
	public String getNomeFantasia() {
		return nomeFantasia;
	}
	
	@Field(offset = 78, length = 1, paddingChar = ';')
	public String getSeparador5() {
		return separador5;
	}
	
	@Field(offset = 79, length = 3, paddingChar = '0')
	public String getCampoFixo1() {
		return campoFixo1;
	}
	
	@Field(offset = 82, length = 1, paddingChar = ';')
	public String getSeparador6() {
		return separador6;
	}
	
	@Field(offset = 83, length = 10)
	public String getTipoNota() {
		tipoNota = "CONSIGNADO";
		return tipoNota;
	}
	
	@Field(offset = 93, length = 1, paddingChar = ';')
	public String getSeparador7() {
		return separador7;
	}
	
	@Field(offset = 94, length = 11, paddingChar = '0')
	public String getCampoFixo2() {
		return campoFixo2;
	}
	
	@Field(offset = 105, length = 1, paddingChar = ';')
	public String getSeparador8() {
		return separador8;
	}
	
	@Field(offset = 106, length = 14, paddingChar = '0')
	public String getCampoFixo3() {
		return campoFixo3;
	}
	
	@Field(offset = 120, length = 1, paddingChar = ';')
	public String getSeparador9() {
		return separador9;
	}
	
	@Field(offset=121, length=20, paddingChar=' ')
	public String getFiller() {
		return filler;
	}
	
	@Field(offset = 141, length = 1, paddingChar = ';')
	public String getSeparador10() {
		return separador10;
	}

	public void setCodigoCota(String codigoCota) {
		this.codigoCota = codigoCota;
	}

	public void setNomeCotaComCpfCnpj(String nomeCotaComCpfCnpj) {
		this.nomeCotaComCpfCnpj = nomeCotaComCpfCnpj;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

}
