package br.com.abril.nds.model.movimentacao;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoMovimentoCota  {
	
	RECEBIMENTO_REPARTE(TipoOperacao.ENTRADA);
	
	private TipoOperacao tipoOperacao;
	
	private TipoMovimentoCota(TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	
	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

}