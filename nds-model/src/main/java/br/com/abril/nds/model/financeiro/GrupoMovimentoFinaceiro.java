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
	 * Postergado de débito da cota
	 */
	POSTERGADO_DEBITO(OperacaoFinaceira.DEBITO),
	
	/**
	 * Postergado de crédito da cota
	 */
	POSTERGADO_CREDITO(OperacaoFinaceira.CREDITO),
	
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
	 * Conta comprando encalhe.
	 */
	COMPRA_ENCALHE_SUPLEMENTAR(OperacaoFinaceira.DEBITO),

	/**
	 * Postergação de dívida a pedido do jornaleiro.
	 */
	POSTERGADO_NEGOCIACAO(OperacaoFinaceira.DEBITO),
	
	/**
	 * Débito calculado sobre faturamento da cota.
	 */
	DEBITO_SOBRE_FATURAMENTO(OperacaoFinaceira.DEBITO),

	/**
	 * Débito calculado sobre faturamento da cota.
	 */
	CREDITO_SOBRE_FATURAMENTO(OperacaoFinaceira.CREDITO),
	
	/**
	 * Compra de Numeros Atrazados(NA)
	 */
	COMPRA_NUMEROS_ATRAZADOS(OperacaoFinaceira.DEBITO),
	
	/**
	 * Venda total de reparte
	 */
	VENDA_TOTAL(OperacaoFinaceira.DEBITO),
	
	
	PENDENTE(OperacaoFinaceira.DEBITO),
	
	
	LANCAMENTO_CAUCAO_LIQUIDA(OperacaoFinaceira.DEBITO),
	
	
	RESGATE_CAUCAO_LIQUIDA(OperacaoFinaceira.CREDITO),
	
	NEGOCIACAO_COMISSAO(OperacaoFinaceira.DEBITO);
	
	private OperacaoFinaceira operacaoFinaceira;
	
	private GrupoMovimentoFinaceiro(OperacaoFinaceira operacaoFinaceira) {
		this.operacaoFinaceira = operacaoFinaceira;
	}
	
	public OperacaoFinaceira getOperacaoFinaceira() {
		return operacaoFinaceira;
	}

}
