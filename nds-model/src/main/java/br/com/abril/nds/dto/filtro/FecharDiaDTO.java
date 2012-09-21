package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FecharDiaDTO implements Serializable {

	private static final long serialVersionUID = 4116643880953265518L;
	
	private Boolean baixaBancaria;
	
	private Boolean geracaoDeCobranca;

	public Boolean getBaixaBancaria() {
		return baixaBancaria;
	}

	public void setBaixaBancaria(Boolean baixaBancaria) {
		this.baixaBancaria = baixaBancaria;
	}

	public Boolean getGeracaoDeCobranca() {
		return geracaoDeCobranca;
	}

	public void setGeracaoDeCobranca(Boolean geracaoDeCobranca) {
		this.geracaoDeCobranca = geracaoDeCobranca;
	}
	
}
