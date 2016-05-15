package br.com.abril.nds.util.export.cobranca.util;

import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistro01;

public class CobRegReportDTO {
	
	private List<CobRegEnvTipoRegistro01> tipoRegistro01 = new ArrayList<>();

	public List<CobRegEnvTipoRegistro01> getTipoRegistro01() {
		return tipoRegistro01;
	}

	public void setTipoRegistro01(List<CobRegEnvTipoRegistro01> tipoRegistro01) {
		this.tipoRegistro01 = tipoRegistro01;
	}
	
}
