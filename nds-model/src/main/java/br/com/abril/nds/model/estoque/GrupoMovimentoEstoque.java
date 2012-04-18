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
	RECEBIMENTO_FISICO(OperacaoEstoque.ENTRADA), 
	
	/**
	 * Envio de reparte à cota
	 */
	ENVIO_JORNALEIRO(OperacaoEstoque.SAIDA),
	/**
	 * Recebimento do encalhe do jornaleiro
	 */
	RECEBIMENTO_ENCALHE(OperacaoEstoque.ENTRADA), 
	
	/**
	 * Sobra de pacote distribuidor
	 */
	SOBRA_DE(OperacaoEstoque.ENTRADA), 
	
	/**
	 * Sobra e m pacote distribuidor
	 */
	SOBRA_EM(OperacaoEstoque.ENTRADA), 
	
	/**
	 * Falta de pacote distribuidor
	 */
	FALTA_DE(OperacaoEstoque.SAIDA), 
	
	
	/**
	 * Falta em pacote distribuidor
	 */
	FALTA_EM(OperacaoEstoque.SAIDA),
	
	/**
	 * Recebimento do reparte cota
	 */
	RECEBIMENTO_REPARTE(OperacaoEstoque.ENTRADA),
	
	/**
	 * Envio do encalhe cota
	 */
	ENVIO_ENCALHE(OperacaoEstoque.SAIDA),
	
	/**
	 * Estorno do reparte da cota ausente
	 */
	ESTORNO_REPARTE_COTA_AUSENTE(OperacaoEstoque.SAIDA),
	
	/**
	 * Restauracao do reparte de cota ausente
	 */
	RESTAURACAO_REPARTE_COTA_AUSENTE(OperacaoEstoque.ENTRADA),
	
	/**
	 * Entrada de estoque suplementar de cota ausente distribuidor
	 */
	SUPLEMENTAR_COTA_AUSENTE(OperacaoEstoque.ENTRADA), 
	
	/**
	 * Restauração de reparte de cota ausente distribuidor
	 */
	REPARTE_COTA_AUSENTE(OperacaoEstoque.SAIDA),
	
	/**
	 * Compra de encalhe da cota no distribuidor.
	 */
	COMPRA_ENCALHE(OperacaoEstoque.ENTRADA),
	
	/**
	 * Venda de encalhe do distruibuidor para a cota.
	 */
	VENDA_ENCALHE(OperacaoEstoque.SAIDA);
	
	private OperacaoEstoque operacaoEstoque;
	
	private GrupoMovimentoEstoque(OperacaoEstoque operacaoEstoque) {
		this.operacaoEstoque = operacaoEstoque;
	}
	
	public OperacaoEstoque getOperacaoEstoque() {
		return operacaoEstoque;
	}

}