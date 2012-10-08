package br.com.abril.nds.model.estoque;


/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum GrupoMovimentoEstoque  {
	
	/**
	 * Recebimento de mercadorias distribuidor
	 */
	RECEBIMENTO_FISICO(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO), 
	
	/**
	 * Envio de reparte à cota pelo distribuidor
	 */
	ENVIO_JORNALEIRO(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO),

	/**
	 * Recebimento do encalhe da cota pelo distribuidor
	 */
	RECEBIMENTO_ENCALHE(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.DEVOLUCAO_ENCALHE), 

	/**
	 * Recebimento do encalhe JURAMENTADO da cota pelo distribuidor
	 */
	RECEBIMENTO_ENCALHE_JURAMENTADO(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.DEVOLUCAO_ENCALHE), 
	
	/**
	 * Sobra de pacote distribuidor
	 */
	SOBRA_DE(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO), 
	
	/**
	 * Sobra em pacote distribuidor
	 */
	SOBRA_EM(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO), 
	
	/**
	 * Falta de pacote distribuidor
	 */
	FALTA_DE(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO), 
	
	/**
	 * Falta em pacote distribuidor
	 */
	FALTA_EM(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO),
	
	/**
	 * Recebimento do reparte cota
	 */
	RECEBIMENTO_REPARTE(OperacaoEstoque.ENTRADA, Dominio.COTA, TipoEstoque.LANCAMENTO),
	
	/**
	 * Envio do encalhe cota
	 */
	ENVIO_ENCALHE(OperacaoEstoque.SAIDA, Dominio.COTA, TipoEstoque.DEVOLUCAO_ENCALHE),
	
	/**
	 * Estorno do reparte da cota ausente
	 */
	ESTORNO_REPARTE_COTA_AUSENTE(OperacaoEstoque.SAIDA, Dominio.COTA, TipoEstoque.SUPLEMENTAR),
	
	/**
	 * Restauracao do reparte de cota ausente
	 */
	RESTAURACAO_REPARTE_COTA_AUSENTE(OperacaoEstoque.ENTRADA, Dominio.COTA, TipoEstoque.SUPLEMENTAR),
	
	/**
	 * Entrada de estoque suplementar de cota ausente distribuidor
	 */
	SUPLEMENTAR_COTA_AUSENTE(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.SUPLEMENTAR), 

	/**
	 * Entrada de estoque suplementar para envio de encalhe antes da data programada
	 */
	SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.SUPLEMENTAR), 
	
	/**
	 * Restauração de reparte de cota ausente distribuidor
	 */
	REPARTE_COTA_AUSENTE(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.SUPLEMENTAR),
	
	/**
	 * Compra de encalhe da cota no distribuidor.
	 */
	COMPRA_ENCALHE(OperacaoEstoque.ENTRADA, Dominio.COTA, TipoEstoque.DEVOLUCAO_ENCALHE),
	
	/**
	 * Venda de encalhe do distruibuidor para a cota.
	 */
	VENDA_ENCALHE(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.DEVOLUCAO_ENCALHE),
	
	/**
	 * Venda de encalhe suplementar do distruibuidor para a cota.
	 */
	VENDA_ENCALHE_SUPLEMENTAR(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.SUPLEMENTAR),
	
	/**
	 * Estorno de encalhe vendido, devido a um cancelamento da venda.
	 */
	ESTORNO_VENDA_ENCALHE(OperacaoEstoque.ENTRADA,Dominio.DISTRIBUIDOR, TipoEstoque.DEVOLUCAO_ENCALHE),
	
	/**
	 * Estorno de encalhe suplementar vendido, devido a um cancelamento da venda.
	 */
	ESTORNO_VENDA_ENCALHE_SUPLEMENTAR(OperacaoEstoque.ENTRADA,Dominio.DISTRIBUIDOR, TipoEstoque.SUPLEMENTAR),

	/**
	 * 
	 */
	ENCALHE_ANTECIPADO(OperacaoEstoque.ENTRADA,Dominio.COTA, TipoEstoque.DEVOLUCAO_ENCALHE),
	
	/**
	 * 
	 */
	COMPRA_SUPLEMENTAR(OperacaoEstoque.ENTRADA,Dominio.COTA, TipoEstoque.SUPLEMENTAR),
	
	
	/**
	 * Estorno de de porodutos suplementar do estoque da cota
	 */
	ESTORNO_COMPRA_SUPLEMENTAR(OperacaoEstoque.SAIDA,Dominio.COTA, TipoEstoque.SUPLEMENTAR),
	
	
	/**
	 * Estorno da compra de encalhe da cota no distribuidor.
	 */
	ESTORNO_COMPRA_ENCALHE(OperacaoEstoque.SAIDA, Dominio.COTA),
	
	/**
	 *
	 */
	NIVELAMENTO_SAIDA(OperacaoEstoque.SAIDA,Dominio.COTA, TipoEstoque.LANCAMENTO),
	
	/**
	 * 
	 */
	NIVELAMENTO_ENTRADA(OperacaoEstoque.ENTRADA,Dominio.COTA, TipoEstoque.LANCAMENTO),

	/**
	 * Estorno da cota para notas de envio.
	 */
	ESTORNO_ENVIO_REPARTE(OperacaoEstoque.SAIDA, Dominio.COTA, TipoEstoque.LANCAMENTO),
	
	/**
	 * Envio de reparte da cota para suplementar do distribuidor.
	 */
	ENTRADA_SUPLEMENTAR_ENVIO_REPARTE(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.SUPLEMENTAR),

	/**
	 * Cancelamento de NFe - Devolução de mercadorias das NFs canceladas para o estoque de lancamento do distribuidor
	 */
	CANCELAMENTO_NOTA_FISCAL_DEVOLUCAO_CONSIGNADO(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO),
	
	/**
	 * Cancelamento de NFe - Devolução de mercadorias das NFs canceladas para o estoque de recolhimento do distribuidor
	 */
	CANCELAMENTO_NOTA_FISCAL_DEVOLUCAO_ENCALHE(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.DEVOLUCAO_ENCALHE),
	
	/**
	 * Cancelamento de NFe, envio do consignado da cota para o ditribuidor
	 */
	CANCELAMENTO_NOTA_FISCAL_ENVIO_CONSIGNADO(OperacaoEstoque.SAIDA, Dominio.COTA, TipoEstoque.LANCAMENTO),
	
	/**
	 * 
	 */
	TRANSFERENCIA_ENTRADA_LANCAMENTO(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO),
	
	/**
	 * 
	 */
	TRANSFERENCIA_SAIDA_LANCAMENTO(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO),

	/**
	 * 
	 */
	TRANSFERENCIA_ENTRADA_SUPLEMENTAR(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.SUPLEMENTAR),
	
	/**
	 * 
	 */
	TRANSFERENCIA_SAIDA_SUPLEMENTAR(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.SUPLEMENTAR),
	
	/**
	 * 
	 */
	TRANSFERENCIA_ENTRADA_RECOLHIMENTO(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.RECOLHIMENTO),

	/**
	 * 
	 */
	TRANSFERENCIA_SAIDA_RECOLHIMENTO(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.RECOLHIMENTO),

	/**
	 * 
	 */
	TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.PRODUTOS_DANIFICADOS),

	/**
	 * 
	 */
	TRANSFERENCIA_SAIDA_PRODUTOS_DANIFICADOS(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.PRODUTOS_DANIFICADOS);

	private OperacaoEstoque operacaoEstoque;
	private Dominio dominio;
	private TipoEstoque tipoEstoque;
	
	private GrupoMovimentoEstoque(OperacaoEstoque operacaoEstoque, Dominio dominio, TipoEstoque tipoEstoque) {
		this.operacaoEstoque = operacaoEstoque;
		this.dominio = dominio;
		this.tipoEstoque = tipoEstoque;
	}
	
	public OperacaoEstoque getOperacaoEstoque() {
		return operacaoEstoque;
	}
	
	/**
	 * @return o domínio do grupo de movimento
	 */
	public Dominio getDominio() {
		return dominio;
	}
	
	/**
	 * @return the tipoEstoque
	 */
	public TipoEstoque getTipoEstoque() {
		return tipoEstoque;
	}

	/**
	 * Domínio do movimento estoque
	 * 
	 * @author francisco.garcia
	 *
	 */
	public static enum Dominio {
		
		DISTRIBUIDOR, 
		
		COTA;
		
	}

}