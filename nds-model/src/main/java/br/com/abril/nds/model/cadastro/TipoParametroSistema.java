package br.com.abril.nds.model.cadastro;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoParametroSistema {

	PATH_IMAGENS_CAPA, 
	
	/**
	 * Número máximo de tentativas de reprogramação lançamento
	 */
	NUMERO_TENTATIVAS_REPROGRAMACAO_LANCAMENTO,
	
	/**
	 * Capacidade de Distribuição dáaria do distribuidor, em número de
	 * exemplares
	 */
	CAPACIDADE_DISTRIBUICAO_DIARIA,
	
	/**
	 * Número de dias permitido para lançamento de uma
	 * diferença de estoque do tipo FALTA DE.
	 */
	NUMERO_DIAS_LANCAMENTO_FALTA_DE,
	
	/**
	 * Número de dias permitido para lançamento de uma
	 * diferença de estoque do tipo FALTA EM.
	 */
	NUMERO_DIAS_LANCAMENTO_FALTA_EM,
	
	/**
	 * Número de dias permitido para lançamento de uma
	 * diferença de estoque do tipo SOBRA DE.
	 */
	NUMERO_DIAS_LANCAMENTO_SOBRA_DE,
	
	/**
	 * Número de dias permitido para lançamento de uma
	 * diferença de estoque do tipo SOBRA EM.
	 */
	NUMERO_DIAS_LANCAMENTO_SOBRA_EM;
	
}