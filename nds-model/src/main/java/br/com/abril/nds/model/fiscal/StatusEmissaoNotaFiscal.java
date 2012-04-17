package br.com.abril.nds.model.fiscal;

public enum StatusEmissaoNotaFiscal {
	
	/**
	 * Pendente de transmissão para geração da NFe
	 */
	PENDENTE_TRANSMISSAO_NFE,
	/**
	 * Nota fiscal transmitida, aguardando o retorno da geração de NFe
	 */
	AGUARDANDO_GERACAO_NFE,
	/**
	 * NFe gerada
	 */
	EMITIDA,
	/**
	 * Nota Fiscal Cancelada
	 */
	CANCELADA; 
	
	
}