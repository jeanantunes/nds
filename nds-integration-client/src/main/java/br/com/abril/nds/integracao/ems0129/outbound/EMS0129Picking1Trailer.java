package br.com.abril.nds.integracao.ems0129.outbound;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0129Picking1Trailer {

	private String tipoRegistro;  
	
	private Integer codigoCota;
	
	private Integer quantidadeRegistros;
	
	
	@Field(offset = 1, length = 1, paddingChar = '3')
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	
	@Field(offset = 2, length = 5, align = Align.RIGHT)
	public Integer getCodigoCota() {
		return codigoCota;
	}
	
	@Field(offset = 7, length = 4, align = Align.RIGHT)
	public Integer getQuantidadeRegistros() {
		return quantidadeRegistros;
	}
	
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	
	public void setCodigoCota(Integer codigoCota) {
		this.codigoCota = codigoCota;
	}
	
	public void setQuantidadeRegistros(Integer quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}	
}
