package br.com.abril.nds.model.fiscal;

public enum GrupoNotaFiscal {

	RECEBIMENTO_MERCADORIAS(TipoOperacao.ENTRADA); 

	private TipoOperacao tipoOperacao;
	
	private GrupoNotaFiscal(TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	
	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

}
