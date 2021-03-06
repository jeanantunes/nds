package br.com.abril.nds.integracao.ems0124.outbound;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0124Trailer {
	
	private String tipoRegistro;
	private String filler;
	
	@Field(offset=1, length=1, paddingChar='9')
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	@Field(offset=2, length=50, paddingChar='0')
	public String getFiller() {
		return filler;
	}


	public void setFiller(String filler) {
		this.filler = filler;
	}
	
}