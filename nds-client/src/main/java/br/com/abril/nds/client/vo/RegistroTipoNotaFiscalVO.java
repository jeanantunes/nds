package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class RegistroTipoNotaFiscalVO implements Serializable {

	private static final long serialVersionUID = -2039605389828028947L;

	@Export(label = "Tipo de Nota", exhibitionOrder = 1)
	private String nopDescricao;
	
	@Export(label = "CFOP Dentro UF", exhibitionOrder = 2)
	private String cfopEstado;
	
	@Export(label = "CFOP Fora UF", exhibitionOrder = 3)
	private String cfopOutrosEstados;

	public String getNopDescricao() {
		return nopDescricao;
	}

	public void setNopDescricao(String nopDescricao) {
		this.nopDescricao = nopDescricao;
	}

	public String getCfopEstado() {
		return cfopEstado;
	}

	public void setCfopEstado(String cfopEstado) {
		this.cfopEstado = cfopEstado;
	}

	public String getCfopOutrosEstados() {
		return cfopOutrosEstados;
	}

	public void setCfopOutrosEstados(String cfopOutrosEstados) {
		this.cfopOutrosEstados = cfopOutrosEstados;
	}

}
