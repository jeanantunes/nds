package br.com.abril.nds.model.fiscal;

public enum GrupoNotaFiscal {

	RECEBIMENTO_MERCADORIAS(TipoOperacao.ENTRADA),

	DEVOLUCAO_MERCADORIA_FORNECEDOR(TipoOperacao.SAIDA),

	RECEBIMENTO_MERCADORIAS_ENCALHE(TipoOperacao.ENTRADA),

	NF_TERCEIRO_COMPLEMENTAR(TipoOperacao.ENTRADA),

	NF_TERCEIRO(TipoOperacao.ENTRADA),

	/**
	 * Notas Fiscais referente às movimentações de Reparte e Suplmentar.
	 */
	NF_REMESSA_CONSIGNACAO(TipoOperacao.SAIDA),

	/**
	 * Notas Fiscais renferente aos encalhes (produtos enviados em consignação e
	 * não devolvidos).
	 */
	NF_DEVOLUCAO_REMESSA_CONSIGNACAO(TipoOperacao.ENTRADA),

	/**
	 * Notas Fiscais referente ao que foi vendido da para o consumidor final dos
	 * itens vendidos na Nota Fiscal de Remessa e Consignação.
	 */
	NF_DEVOLUCAO_SIMBOLICA(TipoOperacao.ENTRADA),

	/**
	 * Notas de Venda a vista e Notas Fiscais referente ao que foi vendido da
	 * para o consumidor final dos itens vendidos na Nota Fiscal de Remessa e
	 * Consignação.
	 */
	NF_VENDA(TipoOperacao.SAIDA),

	/**
	 * Devolução de Mercadoria Recebida em Consignação (Distribuidor -> TREELOG)
	 */
	NF_DEVOLUCAO_MERCADORIA_RECEBIA_CONSIGNACAO(TipoOperacao.SAIDA),
	/**
	 * Remessa de Mercadoria em Consignação
	 */
	NF_REMESSA_MERCADORIA_CONSIGNACAO(TipoOperacao.SAIDA),
	/**
	 * NF-e de Entrada de Retorno de Remessa para Distribuição
	 */
	NF_ENTRADA_RETORNO_REMESSA_DISTRIBUICAO(TipoOperacao.ENTRADA),
	/**
	 * NF-e de Devolução de Remessa para Distribuição
	 */
	NF_DEVOLUCAO_REMESSA_DISTRIBUICAO(TipoOperacao.SAIDA),
	/**
	 * NF-e de Remessa para Distribuição (NECA / Danfe)
	 */
	NF_REMESSA_DISTRIBUICAO(TipoOperacao.SAIDA), 
	/**
	 * NF-e Devolução de Encalhe
	 */
	NF_DEVOLUCAO_ENCALHE(TipoOperacao.SAIDA), 
	/**
	 * Retorno de Remessa para Distribuição
	 */
	NF_RETORNO_REMESSA_DISTRIBUICAO(TipoOperacao.SAIDA),

	;

	private TipoOperacao tipoOperacao;

	private GrupoNotaFiscal(TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

}
