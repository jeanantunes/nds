package br.com.abril.nds.util.export.cobranca.registrada;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.util.export.cobranca.util.CobRegBaseDTO;
import br.com.abril.nds.util.export.cobranca.util.CobRegfield;

public class CobRegEnvTipoRegistro09 extends CobRegBaseDTO {
	
	@CobRegfield(tamanho=1, tipo="char", ordem=1)
	private String tipoRegistro = "9";
	
	@CobRegfield(tamanho = 393, tipo = "char", ordem = 2)
	private String filler;
	
	@CobRegfield(tamanho=6, tipo="numeric", ordem=3)
	private String sequencial;
	
	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = StringUtils.leftPad(filler, 393, ' ');
	}

	public String getSequencial() {
		return sequencial;
	}

	public void setSequencial(String sequencial) {
		this.sequencial = StringUtils.leftPad(sequencial, 6, '0');
	}
}