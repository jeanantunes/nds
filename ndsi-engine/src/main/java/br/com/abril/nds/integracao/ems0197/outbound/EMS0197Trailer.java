package br.com.abril.nds.integracao.ems0197.outbound;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0197Trailer {
	private String tipoRegistro;
	private String numeroCota;
	private int qtdeRegTipo2;
	
	
	@Field(length = 1000, offset = 1)
	public String getTipoRegistro() {
		this.tipoRegistro="3"+"|"+this.numeroCota
							 +"|"+this.qtdeRegTipo2;
		
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
	public int getQtdeRegTipo2() {
		return qtdeRegTipo2;
	}
	public void setQtdeRegTipo2(int qtdeRegTipo2) {
		this.qtdeRegTipo2 = qtdeRegTipo2;
	}
	
	
	
}