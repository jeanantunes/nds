package br.com.abril.nds.model.fiscal;

public enum GrupoNotaFiscal {

	RECEBIMENTO_MERCADORIAS(TipoOperacao.ENTRADA), 
	
	DEVOLUCAO_MERCADORIA_FORNECEDOR(TipoOperacao.SAIDA),

	RECEBIMENTO_MERCADORIAS_ENCALHE(TipoOperacao.ENTRADA),
	
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
	 * Notas de Venda a vista e Notas Fiscais referente ao que foi vendido da para o consumidor final dos
	 * itens vendidos na Nota Fiscal de Remessa e Consignação.
	 */
	NF_VENDA(TipoOperacao.SAIDA);
	
	
	
	private TipoOperacao tipoOperacao;
	
	private GrupoNotaFiscal(TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	
	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

}
