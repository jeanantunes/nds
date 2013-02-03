package br.com.abril.nds.integracao.ems0127.outbound;

import java.io.Serializable;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0127Header implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tipoRegistro;
	private Date dataGeraArq;
	private Date horaGeraArq;
	private Integer qtdeRegistrosDetalhe;
	
	@Field(offset = 1, length = 1, paddingChar = '1')
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	
	@Field(offset = 2, length = 8)
	@FixedFormatPattern("ddMMyyyy")
	public Date getDataGeraArq() {
		return dataGeraArq;
	}
	
	@Field(offset = 10, length = 8)
	@FixedFormatPattern("HHmmssSS")
	public Date getHoraGeraArq() {
		return horaGeraArq;
	}
	
	@Field(offset = 18, length = 6)
	public Integer getQtdeRegistrosDetalhe() {
		return qtdeRegistrosDetalhe;
	}
	
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	
	public void setDataGeraArq(Date dataGeraArq) {
		this.dataGeraArq = dataGeraArq;
	}
	
	public void setHoraGeraArq(Date horaGeraArq) {
		this.horaGeraArq = horaGeraArq;
	}
	
	public void setQtdeRegistrosDetalhe(Integer qtdeRegistrosDetalhe) {
		this.qtdeRegistrosDetalhe = qtdeRegistrosDetalhe;
	}

	
}