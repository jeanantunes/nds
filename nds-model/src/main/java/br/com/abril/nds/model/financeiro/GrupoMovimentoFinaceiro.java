package br.com.abril.nds.model.financeiro;

public enum GrupoMovimentoFinaceiro {
	
	/**
	 * Recebimento do reparte cota
	 */
	RECEBIMENTO_REPARTE(OperacaoFinaceira.DEBITO),
	
	/**
	 * Envio do encalhe cota
	 */
	ENVIO_ENCALHE(OperacaoFinaceira.CREDITO),
	
	/**
	 * Postergado da cota
	 */
	POSTERGADO(OperacaoFinaceira.DEBITO),
	
	/**
	 * Crédito cota
	 */
	CREDITO(OperacaoFinaceira.CREDITO),
	
	/**
	 * Débito cota
	 */
	DEBITO(OperacaoFinaceira.DEBITO), 
	
	/**
	 * Juros cota
	 */
	JUROS(OperacaoFinaceira.DEBITO), 
	
	/**
	 * Multa cota
	 */
	MULTA(OperacaoFinaceira.DEBITO), 
	
	/**
	 * Estorno do reparte de cota ausente
	 */
	ESTORNO_REPARTE_COTA_AUSENTE(OperacaoFinaceira.CREDITO),
	
	/**
	 * Recuperação do reparte de cota ausente
	 */
	RECUPERACAO_REPARTE_COTA_AUSENTE(OperacaoFinaceira.DEBITO),	

	/**
	 * Conta comprando encalhe.
	 */
	COMPRA_ENCALHE(OperacaoFinaceira.DEBITO),

	/**
	 * Postergação de dívida a pedido do jornaleiro.
	 */
	POSTERGADO_NEGOCIACAO(OperacaoFinaceira.DEBITO),
	
	/**
	 * Débito calculado sobre faturamento da cota.
	 */
	DEBITO_SOBRE_FATURAMENTO(OperacaoFinaceira.DEBITO),
	
	
	/**
	 * Compra de Numeros Atrazados(NA)
	 */
	COMPRA_NUMEROS_ATRAZADOS(OperacaoFinaceira.DEBITO);
	
	
	private OperacaoFinaceira operacaoFinaceira;
	
	private GrupoMovimentoFinaceiro(OperacaoFinaceira operacaoFinaceira) {
		this.operacaoFinaceira = operacaoFinaceira;
	}
	
	public OperacaoFinaceira getOperacaoFinaceira() {
		return operacaoFinaceira;
	}

}
