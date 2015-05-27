package br.com.abril.nds.model.estoque;

import br.com.abril.nds.enums.Dominio;



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
	 * Estorno do recebimento de mercadorias distribuidor
	 */
	ESTORNO_RECEBIMENTO_FISICO(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO), 
	
	/**
	 * Estorno do recebimento de mercadorias distribuidor (Reparte promocional)
	 */
	ESTORNO_REPARTE_PROMOCIONAL(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO), 
	
	/**
	 * Envio de reparte à cota pelo distribuidor
	 */
	ENVIO_JORNALEIRO(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO),
	
	/**
	 * Envio de reparte do tipo conta firme à cota pelo distribuidor
	 */
	ENVIO_JORNALEIRO_PRODUTO_CONTA_FIRME(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO),

	/**
	 * Ocorre durante o a funcionalidade "Fechamento Encalhe" (Fechamento Estoque Físico X Lógico)
	 * representando a entrada de produtos na cota que antes saíram de forma juramentada.
	 */
	RECEBIMENTO_JORNALEIRO_JURAMENTADO(OperacaoEstoque.ENTRADA, Dominio.COTA, TipoEstoque.JURAMENTADO),


	/**
	 * Ocorre durante o a funcionalidade "Fechamento Encalhe" (Fechamento Estoque Físico X Lógico)
	 * representando a saída de produtos do distribuidor que antes entraram de forma juramentada.
	 */
	ENVIO_JORNALEIRO_JURAMENTADO(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.JURAMENTADO),

	
	/**
	 * Recebimento do encalhe da cota pelo distribuidor
	 */
	RECEBIMENTO_ENCALHE(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.DEVOLUCAO_ENCALHE), 

	/**
	 * Recebimento do encalhe JURAMENTADO da cota pelo distribuidor
	 */
	RECEBIMENTO_ENCALHE_JURAMENTADO(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.JURAMENTADO), 
	
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
	 * Ganho em pacote distribuidor
	 */
	GANHO_EM(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.GANHO),
	
	/**
	 * Ganho de pacote distribuidor
	 */
	GANHO_DE(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.GANHO),
	
	/**
	 * Perda em pacote distribuidor
	 */
	PERDA_EM(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.PERDA),
	
	/**
	 * Perda de pacote distribuidor
	 */
	PERDA_DE(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.PERDA),
	
	/**
	 * Perda EM no estoque de devolução --> PERDA
	 */
	PERDA_EM_DEVOLUCAO(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.DEVOLUCAO_ENCALHE),
	
	/**
	 * Sobra de pacote distribuidor
	 */
	SOBRA_DE_COTA(OperacaoEstoque.ENTRADA, Dominio.COTA, TipoEstoque.LANCAMENTO), 
	
	/**
	 * Sobra em pacote distribuidor
	 */
	SOBRA_EM_COTA(OperacaoEstoque.ENTRADA, Dominio.COTA, TipoEstoque.LANCAMENTO),
	
	/**
	 * Sobra de pacote direcionada para cota
	 */
	SOBRA_DE_DIRECIONADA_PARA_COTA(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.COTA),	

	/**
	 * Contra-partida dos movimentos de sobra, quando direcionados para Cota.
	 * 
	 */
	SOBRA_ENVIO_PARA_COTA(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.GANHO),
	
	/**
	 * Sobra em pacote direcionada para cota
	 */
	SOBRA_EM_DIRECIONADA_PARA_COTA(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO),
	
	/**
	 * Sobra em direcionada para estoque de devolução de encalhe
	 */
	SOBRA_EM_DEVOLUCAO(OperacaoEstoque.ENTRADA,Dominio.DISTRIBUIDOR,TipoEstoque.DEVOLUCAO_ENCALHE),
	
	/**
	 * Falta de pacote distribuidor
	 */
	FALTA_DE_COTA(OperacaoEstoque.SAIDA, Dominio.COTA, TipoEstoque.LANCAMENTO), 
	
	/**
	 * Falta em pacote distribuidor
	 */
	FALTA_EM_COTA(OperacaoEstoque.SAIDA, Dominio.COTA, TipoEstoque.LANCAMENTO),
	
	/**
	 * Falta em pacote direcionada para cota
	 */
	FALTA_EM_DIRECIONADA_PARA_COTA(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO),
	
	/**
	 * Contra-partida dos movimentos de falta, quando direcionados para cota.
	 */
	FALTA_EM_DIRECIONADA_COTA(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.PERDA),
	
	/**
	 * Contra-partida dos movimentos de falta, quando direcionados para cota.
	 */
	AJUSTE_REPARTE_FALTA_COTA(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO),
	
	/**
	 * Recebimento do reparte cota
	 */
	RECEBIMENTO_REPARTE(OperacaoEstoque.ENTRADA, Dominio.COTA, TipoEstoque.LANCAMENTO),
	
	/**
	 * Distribuicao do reparte cota
	 */
	RECEBIMENTO_REPARTE_DISTRIBUICAO(OperacaoEstoque.ENTRADA, Dominio.COTA, TipoEstoque.LANCAMENTO),
	
	/**
	 * Recebimento do reparte do tipo conta firme pela cota
	 */
	RECEBIMENTO_REPARTE_CONTA_FIRME(OperacaoEstoque.ENTRADA, Dominio.COTA, TipoEstoque.LANCAMENTO),
	
	/**
	 * Envio do encalhe cota
	 */
	ENVIO_ENCALHE(OperacaoEstoque.SAIDA, Dominio.COTA, TipoEstoque.DEVOLUCAO_ENCALHE),
	
	/**
	 * Estorno do reparte da cota ausente
	 */
	ESTORNO_REPARTE_COTA_AUSENTE(OperacaoEstoque.SAIDA, Dominio.COTA, TipoEstoque.SUPLEMENTAR),
	
	/**
	 * Rateio de reparte da cota ausente
	 */
	RATEIO_REPARTE_COTA_AUSENTE(OperacaoEstoque.ENTRADA, Dominio.COTA, TipoEstoque.LANCAMENTO),

	/**
	 * Estorno do reparte por furo de publicação
	 */
	ESTORNO_REPARTE_FURO_PUBLICACAO(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO),
	
	/**
	 * Estorno do reparte da cota por furo de publicação 
	 */
	ESTORNO_REPARTE_COTA_FURO_PUBLICACAO(OperacaoEstoque.SAIDA, Dominio.COTA, TipoEstoque.LANCAMENTO),
	
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
	 * Devolução de encalhe do distruibuidor para o fornecedor.
	 */
	DEVOLUCAO_ENCALHE(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.DEVOLUCAO_FORNECEDOR),

	/**
	 * Estorno de encalhe vendido, devido a um cancelamento da venda.
	 */
	ESTORNO_VENDA_ENCALHE(OperacaoEstoque.ENTRADA,Dominio.DISTRIBUIDOR, TipoEstoque.DEVOLUCAO_ENCALHE),
	
	/**
	 * Estorno de devolução de encalhe , devido a um cancelamento da chamada de encalhe fornecedor.
	 */
	ESTORNO_DEVOLUCAO_ENCALHE_FORNECEDOR(OperacaoEstoque.ENTRADA,Dominio.DISTRIBUIDOR, TipoEstoque.DEVOLUCAO_ENCALHE),
	
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
	ESTORNO_COMPRA_ENCALHE(OperacaoEstoque.SAIDA, Dominio.COTA, TipoEstoque.LANCAMENTO),
	
	/**
	 *
	 */
	NIVELAMENTO_SAIDA(OperacaoEstoque.SAIDA, Dominio.COTA, TipoEstoque.LANCAMENTO),
	
	/**
	 * 
	 */
	NIVELAMENTO_ENTRADA(OperacaoEstoque.ENTRADA, Dominio.COTA, TipoEstoque.LANCAMENTO),

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
	TRANSFERENCIA_PARCIAL_SAIDA_LANCAMENTO(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO),

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
	TRANSFERENCIA_PARCIAL_SAIDA_SUPLEMENTAR(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.SUPLEMENTAR),
	
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
	TRANSFERENCIA_SAIDA_PRODUTOS_DANIFICADOS(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.PRODUTOS_DANIFICADOS),
	
	/**
	 * 
	 */
	TRANSFERENCIA_PARCIAL_SAIDA_PRODUTOS_DANIFICADOS(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.PRODUTOS_DANIFICADOS),
	
	/**
	 * 
	 */
	TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_FORNECEDOR(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.DEVOLUCAO_FORNECEDOR),

	/**
	 * 
	 */
	TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_FORNECEDOR(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.DEVOLUCAO_FORNECEDOR), 
	
	TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_ENCALHE(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.DEVOLUCAO_ENCALHE),
	
	TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_ENCALHE(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.DEVOLUCAO_ENCALHE),
	
	TRANSFERENCIA_PARCIAL_SAIDA_PRODUTOS_DEVOLUCAO_ENCALHE(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.DEVOLUCAO_ENCALHE),
	
	TRANSFERENCIA_ENTRADA_ESTOQUE_PARCIAIS(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO),

	GRUPO_MATERIAL_PROMOCIONAL(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR, TipoEstoque.MATERIAL_PROMOCIONAL),
	
	/**
	 * Entrada de estoque suplementar rollout do sistema
	 * Criado em conjunto com Cesar Marracho
	 */
	ENTRADA_ESTOQUE_SUPLEMENTAR(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.SUPLEMENTAR),

	/**
	 * Grupos de movimento de estoque para alterações de reparte das cotas (lançamento de faltas e sobras).
	 */
	ALTERACAO_REPARTE_COTA(OperacaoEstoque.SAIDA, Dominio.COTA, TipoEstoque.LANCAMENTO),
	ALTERACAO_REPARTE_COTA_PARA_LANCAMENTO(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.LANCAMENTO),
	ALTERACAO_REPARTE_COTA_PARA_RECOLHIMENTO(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.RECOLHIMENTO),
	ALTERACAO_REPARTE_COTA_PARA_SUPLEMENTAR(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.SUPLEMENTAR),
	ALTERACAO_REPARTE_COTA_PARA_PRODUTOS_DANIFICADOS(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR, TipoEstoque.PRODUTOS_DANIFICADOS);
	
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

}