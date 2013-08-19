package br.com.abril.nds.model.fiscal;

public enum TipoEmissaoNfe {

	NORMAL("Normal"),
	CONTINGENCIA_FORMULARIO_SEGURANCA("Contingência Formulário de Segurança"),
	CONTINGENCIA_SCAN("Contingência SCAN"),
	CONTINGENCIA_DPEC("Contingência DPEC"),
	CONTINGENCIA_FORMULARIO_SEGURANCA_DA("Contingência Formulário de Segurança DA");
	
	private String descricao;
	
	private TipoEmissaoNfe(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Obtém descricao
	 *
	 * @return String
	 */
	public String getDescricao() {
		return descricao;
	}
	
	
	
}
