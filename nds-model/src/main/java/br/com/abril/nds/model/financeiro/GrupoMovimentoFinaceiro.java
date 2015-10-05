package br.com.abril.nds.model.financeiro;

public enum GrupoMovimentoFinaceiro {
	
	/**
	 * Recebimento do reparte cota
	 */
	RECEBIMENTO_REPARTE(OperacaoFinaceira.DEBITO, "Recebimento Reparte"),
	
	/**
	 * Envio do encalhe cota
	 */
	ENVIO_ENCALHE(OperacaoFinaceira.CREDITO, "Envio Encalhe"),
	
	/**
	 * Postergado de débito da cota
	 */
	POSTERGADO_DEBITO(OperacaoFinaceira.DEBITO, "Pagamento Postergado"),
	
	/**
	 * Postergado de crédito da cota
	 */
	POSTERGADO_CREDITO(OperacaoFinaceira.CREDITO, "Crédito Postergado"),
	
	/**
	 * Crédito cota
	 */
	CREDITO(OperacaoFinaceira.CREDITO, OperacaoFinaceira.CREDITO.getDescricao()),
	
	/**
	 * Débito cota
	 */
	DEBITO(OperacaoFinaceira.DEBITO, OperacaoFinaceira.DEBITO.getDescricao()), 
	
	/**
	 * Juros cota
	 */
	JUROS(OperacaoFinaceira.DEBITO, "Juros"), 
	
	/**
	 * Multa cota
	 */
	MULTA(OperacaoFinaceira.DEBITO, "Multa"), 	

	/**
	 * Conta comprando encalhe.
	 */
	COMPRA_ENCALHE_SUPLEMENTAR(OperacaoFinaceira.DEBITO, "Compra de Encalhe Suplementar"),

	/**
	 * Postergação de dívida a pedido do jornaleiro.
	 */
	POSTERGADO_NEGOCIACAO(OperacaoFinaceira.DEBITO, "Postergado Negociação"),
	
	/**
	 * Débito calculado sobre faturamento da cota.
	 */
	DEBITO_SOBRE_FATURAMENTO(OperacaoFinaceira.DEBITO, "Débito Sobre Faturamento"),

	/**
	 * Débito calculado sobre faturamento da cota.
	 */
	CREDITO_SOBRE_FATURAMENTO(OperacaoFinaceira.CREDITO, "Crédito Sobre Faturamento"),
	
	/**
	 * Compra de Numeros Atrazados(NA)
	 */
	COMPRA_NUMEROS_ATRAZADOS(OperacaoFinaceira.DEBITO, "Compra Números Atrasados"),
	
	/**
	 * Venda total de reparte
	 */
	VENDA_TOTAL(OperacaoFinaceira.DEBITO, "Venda Total"),
	
	
	PENDENTE(OperacaoFinaceira.DEBITO, "Pendente"),
	
	
	LANCAMENTO_CAUCAO_LIQUIDA(OperacaoFinaceira.DEBITO, "Lançamento Caução Líquida"),
	
	
	RESGATE_CAUCAO_LIQUIDA(OperacaoFinaceira.CREDITO, "Resgate Caução Líquida"),
	
	NEGOCIACAO_COMISSAO(OperacaoFinaceira.DEBITO, "Comissão Negociação"),
	
	DEBITO_COTA_TAXA_DE_ENTREGA_TRANSPORTADOR(OperacaoFinaceira.DEBITO, "Débito Taxa de Entrega - Transportador"),
	
	DEBITO_COTA_TAXA_DE_ENTREGA_ENTREGADOR(OperacaoFinaceira.DEBITO, "Débito Taxa de Entrega - Entregador"),
	
	TAXA_EXTRA(OperacaoFinaceira.DEBITO, "Taxa Extra");
	
	private OperacaoFinaceira operacaoFinaceira;
	
	private String descricao;
	
	private GrupoMovimentoFinaceiro(OperacaoFinaceira operacaoFinaceira,
	        String descricao) {
		this.operacaoFinaceira = operacaoFinaceira;
		this.descricao = descricao;
	}
	
	public OperacaoFinaceira getOperacaoFinaceira() {
		return operacaoFinaceira;
	}
	
	public String getDescricao(){
	    return this.descricao;
	}

}
