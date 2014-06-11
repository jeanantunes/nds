package br.com.abril.nds.model.fiscal;

public enum StatusEmissaoNotaFiscal {
	
	/**
	 * Pendente de transmissão para geração da NFe
	 */
	PENDENTE_TRANSMISSAO_NFE("Transmissão NFE pendente"),
	/**
	 * Nota fiscal transmitida, aguardando o retorno da geração de NFe
	 */
	AGUARDANDO_GERACAO_NFE("Aguardando Geração Nfe"),
	/**
	 * NFe gerada
	 */
	EMITIDA("Emitida"),
	/**
	 * Nota Fiscal Cancelada
	 */
	CANCELADA("Cancelada"); 
	
	
	
	private StatusEmissaoNotaFiscal(String descricao) {
		this.descricao = descricao;
	}
	
	private String descricao;

	/**
	 * Obtém descricao
	 *
	 * @return String
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Atribuí descricao
	 * @param descricao 
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	
	
}