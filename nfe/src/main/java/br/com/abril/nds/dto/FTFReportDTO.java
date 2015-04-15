package br.com.abril.nds.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro01;

public class FTFReportDTO {
	
	private List<FTFEnvTipoRegistro01> naoCadastradosCRP = new ArrayList<>();

	private int pedidosGerados = 0;
	
	public List<FTFEnvTipoRegistro01> getNaoCadastradosCRP() {
		return naoCadastradosCRP;
	}

	public void setNaoCadastradosCRP(List<FTFEnvTipoRegistro01> naoCadastradosCRP) {
		this.naoCadastradosCRP = naoCadastradosCRP;
	}

	public int getPedidosGerados() {
		return pedidosGerados;
	}

	public void setPedidosGerados(int pedidosGerados) {
		this.pedidosGerados = pedidosGerados;
	}
	
}
