package br.com.abril.nds.integracao.ems0120.outbound;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0120Trailer {
	private String tipoRegistro;

	@Field(offset=1, length=1, paddingChar='9')
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	
	@Field(offset=2, length=51, paddingChar='0')
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
}