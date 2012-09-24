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
	RECEBIMENTO_FISICO(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR), 
	
	/**
	 * Envio de reparte à cota pelo distribuidor
	 */
	ENVIO_JORNALEIRO(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR),

	/**
	 * Recebimento do encalhe da cota pelo distribuidor
	 */
	RECEBIMENTO_ENCALHE(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR), 

	/**
	 * Recebimento do encalhe JURAMENTADO da cota pelo distribuidor
	 */
	RECEBIMENTO_ENCALHE_JURAMENTADO(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR), 
	
	/**
	 * Sobra de pacote distribuidor
	 */
	SOBRA_DE(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR), 
	
	/**
	 * Sobra em pacote distribuidor
	 */
	SOBRA_EM(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR), 
	
	/**
	 * Falta de pacote distribuidor
	 */
	FALTA_DE(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR), 
	
	/**
	 * Falta em pacote distribuidor
	 */
	FALTA_EM(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR),
	
	/**
	 * Recebimento do reparte cota
	 */
	RECEBIMENTO_REPARTE(OperacaoEstoque.ENTRADA, Dominio.COTA),
	
	/**
	 * Envio do encalhe cota
	 */
	ENVIO_ENCALHE(OperacaoEstoque.SAIDA, Dominio.COTA),
	
	/**
	 * Estorno do reparte da cota ausente
	 */
	ESTORNO_REPARTE_COTA_AUSENTE(OperacaoEstoque.SAIDA, Dominio.COTA),
	
	/**
	 * Restauracao do reparte de cota ausente
	 */
	RESTAURACAO_REPARTE_COTA_AUSENTE(OperacaoEstoque.ENTRADA, Dominio.COTA),
	
	/**
	 * Entrada de estoque suplementar de cota ausente distribuidor
	 */
	SUPLEMENTAR_COTA_AUSENTE(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR), 

	/**
	 * Entrada de estoque suplementar para envio de encalhe antes da data programada
	 */
	SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR), 
	
	/**
	 * Restauração de reparte de cota ausente distribuidor
	 */
	REPARTE_COTA_AUSENTE(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR),
	
	/**
	 * Compra de encalhe da cota no distribuidor.
	 */
	COMPRA_ENCALHE(OperacaoEstoque.ENTRADA, Dominio.COTA),
	
	/**
	 * Venda de encalhe do distruibuidor para a cota.
	 */
	VENDA_ENCALHE(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR),
	
	/**
	 * Venda de encalhe suplementar do distruibuidor para a cota.
	 */
	VENDA_ENCALHE_SUPLEMENTAR(OperacaoEstoque.SAIDA, Dominio.DISTRIBUIDOR),
	
	/**
	 * Estorno de encalhe vendido, devido a um cancelamento da venda.
	 */
	ESTORNO_VENDA_ENCALHE(OperacaoEstoque.ENTRADA,Dominio.DISTRIBUIDOR),
	
	/**
	 * Estorno de encalhe suplementar vendido, devido a um cancelamento da venda.
	 */
	ESTORNO_VENDA_ENCALHE_SUPLEMENTAR(OperacaoEstoque.ENTRADA,Dominio.DISTRIBUIDOR),

	/**
	 * 
	 */
	ENCALHE_ANTECIPADO(OperacaoEstoque.ENTRADA,Dominio.COTA),
	
	/**
	 * 
	 */
	COMPRA_SUPLEMENTAR(OperacaoEstoque.ENTRADA,Dominio.COTA),
	
	
	/**
	 * Estorno de de porodutos suplementar do estoque da cota
	 */
	ESTORNO_COMPRA_SUPLEMENTAR(OperacaoEstoque.SAIDA,Dominio.COTA),
	
	/**
	 *
	 */
	NIVELAMENTO_SAIDA(OperacaoEstoque.SAIDA,Dominio.COTA),
	
	/**
	 * 
	 */
	NIVELAMENTO_ENTRADA(OperacaoEstoque.ENTRADA,Dominio.COTA),

	/**
	 * Estorno da cota para notas de envio.
	 */
	ESTORNO_ENVIO_REPARTE(OperacaoEstoque.SAIDA, Dominio.COTA),
	
	/**
	 * Envio de reparte da cota para suplementar do distribuidor.
	 */
	ENTRADA_SUPLEMENTAR_ENVIO_REPARTE(OperacaoEstoque.ENTRADA, Dominio.DISTRIBUIDOR);

	
	private OperacaoEstoque operacaoEstoque;
	private Dominio dominio;
	
	private GrupoMovimentoEstoque(OperacaoEstoque operacaoEstoque, Dominio dominio) {
		this.operacaoEstoque = operacaoEstoque;
		this.dominio = dominio;
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