package br.com.abril.nds.integracao.ems0198.outbound;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0198Trailer {

	
	private String registro;  
	
	private String codigoCota;
	
	private String quantidadeRegistros;

	
	@Field(offset = 1, length = 1000)
	public String getRegistro() {
		
		registro = "3|" + this.codigoCota + "|" + this.quantidadeRegistros;
		
		return registro;
	}

	public String getCodigoCota() {
		return codigoCota;
	}

	public String getQuantidadeRegistros() {
		return quantidadeRegistros;
	}
	
	public void setCodigoCota(String codigoCota) {
		this.codigoCota = codigoCota;
	}
	
	public void setQuantidadeRegistros(String quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}	
}
