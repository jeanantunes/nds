package br.com.abril.nds.model.movimentacao;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoMovimentoEstoque  {
	
	ENVIO_JORNALEIRO(TipoOperacao.SAIDA),
	ENCALHE_JORNALEIRO(TipoOperacao.ENTRADA), 
	SOBRA_DE(TipoOperacao.ENTRADA), 
	SOBRA_EM(TipoOperacao.ENTRADA), 
	FALTA_DE(TipoOperacao.SAIDA), 
	FALTA_EM(TipoOperacao.SAIDA),
	FURO(TipoOperacao.ENTRADA);
	
	private TipoOperacao tipoOperacao;
	
	private TipoMovimentoEstoque(TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	
	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

}