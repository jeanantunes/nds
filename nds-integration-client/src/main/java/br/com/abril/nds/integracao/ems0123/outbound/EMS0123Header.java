package br.com.abril.nds.integracao.ems0123.outbound;

import java.io.Serializable;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0123Header implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tipoRegistro;
	private Date dataMovimento;
	private Date horaMovimento;
	private Integer qtdeRegistrosDetalhe;
	private String filler;
	
	@Field(offset=1, length=1, paddingChar='1')
	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	@Field(offset=2, length=8)
	@FixedFormatPattern("ddMMyyyy")
	public Date getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	@Field(offset=10, length=8)
	@FixedFormatPattern("HHmmssSS")
	public Date getHoraMovimento() {
		return horaMovimento;
	}

	public void setHoraMovimento(Date horaMovimento) {
		this.horaMovimento = horaMovimento;
	}
	
	@Field(offset=18, length=6, paddingChar='0')
	public Integer getQtdeRegistrosDetalhe() {
		return qtdeRegistrosDetalhe;
	}
	
	public void setQtdeRegistrosDetalhe(Integer qtdeRegistrosDetalhe) {
		this.qtdeRegistrosDetalhe = qtdeRegistrosDetalhe;
	}

	@Field(offset=24, length=18, paddingChar='0')
	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

	
}