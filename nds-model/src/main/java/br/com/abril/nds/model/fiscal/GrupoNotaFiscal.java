package br.com.abril.nds.model.fiscal;

public enum GrupoNotaFiscal {

	RECEBIMENTO_MERCADORIAS(TipoOperacao.ENTRADA), 
	
	DEVOLUCAO_MERCADORIA_FORNECEDOR(TipoOperacao.SAIDA);

	private TipoOperacao tipoOperacao;
	
	private GrupoNotaFiscal(TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	
	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

}
